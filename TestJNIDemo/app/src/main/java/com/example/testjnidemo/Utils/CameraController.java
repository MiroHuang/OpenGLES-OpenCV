package com.example.testjnidemo.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.testjnidemo.MainActivity;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraController implements SurfaceTexture.OnFrameAvailableListener, GLSurfaceView.Renderer {

    private final String TAG = "CameraController";
    public Context mContext;
    private GLSurfaceView mGLSurfaceView;
    public SurfaceTexture mSurfaceTexture;
    private int mSurfaceTextureId;
    public Camera mCamera;

    private int mDisplayOrientation;//旋转角度
    private int mRotation;
    private int mPreViewWidth;//预览宽度
    private int mPreViewHeight;//预览高度
    private int mPreViewFps;//帧率

    private MyRender mRenderer;
    private final float[] mTexMtx = CameraUtils.createIdentityMtx();

    public int mCamera_status = -1;

    private CameraController() {
        mDisplayOrientation = 270;
        mRotation = 90;
        mPreViewWidth = 480;
        mPreViewHeight = 640;
        mPreViewFps = 20;
    }

    public static synchronized CameraController getInstance() {
        return CameraControllerHolder.mSington;
    }

    private static class CameraControllerHolder {
        private static final CameraController mSington = new CameraController();
    }

    public boolean openCamera(Context context, GLSurfaceView glSurfaceView) {
        Log.e(TAG, "openCamera：" + Thread.currentThread());

        boolean resultValue = true;
        try {
            mContext = context;
            mGLSurfaceView = glSurfaceView;
            //Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(2);
            //为了在GLSurfaceView中绘制图形而设置Renderer(渲染类)
            //设置与此视图关联的渲染器。同时启动将调用渲染器的线程，这反过来导致渲染开始。
            mGLSurfaceView.setRenderer(this);
            //设置为手动刷新模式
            mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            mGLSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    closeCamera();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            resultValue = false;
        }
        return resultValue;
    }

    //TODO 监测摄像头有新的数据到来
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mGLSurfaceView.requestRender();
    }

    //TODO 当Surface被创建的时候，GLSurfaceView会调用这个方法
    //TODO 这发生在应用程序创建的第一次，并且当设备被唤醒或者用户从其他activity切换回去时，也会被调用。
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG, "onSurfaceCreated：" + Thread.currentThread());
        // 初始化摄像头预览扩展纹理
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        //摄像头纹理
        mSurfaceTextureId = textures[0];
        mSurfaceTexture = new SurfaceTexture(mSurfaceTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mSurfaceTextureId);
        //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    //TODO 在Surface创建以后，每次Surface尺寸变化后，这个方法都会调用
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG, "onSurfaceChanged");
        try {
            mRenderer = new MyRender(mContext.getApplicationContext(), setCameraParameters(), width, height);
            // 开始预览（开始捕获并将预览帧绘制到屏幕上）
            mCamera.startPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO 当绘制每一帧的时候会被调用。
    @SuppressLint("DefaultLocale")
    @Override
    public void onDrawFrame(GL10 gl) {
        //Log.i(TAG, "onDrawFrame : " + Thread.currentThread());

//        List<int[]> fpsList = mCamera.getParameters().getSupportedPreviewFpsRange();
//        for (int i = 0; i < fpsList.size(); i++) {
//            final int[] fps = fpsList.get(i);
//            ((MainActivity) mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((MainActivity) mContext).mTextView.setText(String.format("camera preview fps min=%d,max=%d",fps[0],fps[1]));
//                }
//            });
//            //Log.i("FPS", String.format("camera preview fps min=%d,max=%d",fps[0],fps[1]));
//        }
        try {
            GLES20.glClearColor(0f, 0f, 0f, 1f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            mSurfaceTexture.updateTexImage();
            mSurfaceTexture.getTransformMatrix(mTexMtx);
            //渲染到屏幕上setPreviewTexture
            mRenderer.draw(mTexMtx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Camera.Size setCameraParameters() throws IOException {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera_status = i;
                mCamera = Camera.open(i);
                break;
//            }
        }
        // 设置用于实时预览的表面纹理
        mCamera.setPreviewTexture(mSurfaceTexture);
        // 设置相机顺时针旋转度数
        mCamera.setDisplayOrientation(mDisplayOrientation);
        Camera.Size mPreviewSize = CameraUtils.getOptimalPreviewSize(mCamera, mPreViewWidth, mPreViewHeight);

        // 设置相机参数
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(mRotation);
        parameters.setPreviewFormat(ImageFormat.NV21);
        assert mPreviewSize != null;
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        // 设置最小和最大预览fps
        // 这可以控制{@link PreviewCallback}中收到的预览帧的速率。
        // 最小和最大预览fps必须是{@link #getSupportedPreviewFpsRange}中的元素之一。
        int[] range = CameraUtils.adaptPreviewFps(mPreViewFps, parameters.getSupportedPreviewFpsRange());
        parameters.setPreviewFpsRange(range[0], range[1]);
        return mPreviewSize;
    }

    public void switchCamera() {
        Log.e(TAG, "onSurfaceChanged");
        closeCamera();
        try {
            setCameraParameters();
            // 开始预览（开始捕获并将预览帧绘制到屏幕上）
            mCamera.startPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
