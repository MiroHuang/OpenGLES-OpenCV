package com.example.testjnidemo.Utils;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class MyRender {
    private static final String TAG = "MyRender";
    private int mScreenW = -1;
    private int mScreenH = -1;

    private int mProgram;

    private int muPosMtxHandle = -1;
    private int muTexMtxHandle = -1;

    private FloatBuffer mPosBuffer;
    private FloatBuffer mTexBuffer;

    private FloatBuffer mCameraTexCoordBuffer; //纹理坐标,根据窗口大小和图像大小生成
    private final float[] mPosCoordinate = CameraUtils.createIdentityMtx();

    private boolean mIrrorImage = false;//是否开启镜像
    public boolean mBoolean = false;//切换相机

    public MyRender(Context context, Camera.Size mPreviewSize, int width, int height) {
        mScreenW = width;
        mScreenH = height;

        mProgram = ShaderUtils.createProgram(context.getApplicationContext(), "vertex_texture.glsl", "fragment_texture.glsl");
        initCameraTexCoordBuffer(mPreviewSize);
        initAndResetGLProgram();
    }

    private void initCameraTexCoordBuffer(Camera.Size mPreviewSize) {
        //TODO 横竖屏对宽高的调整
        int cameraWidth = Math.min(mPreviewSize.width, mPreviewSize.height);
        int cameraHeight = Math.max(mPreviewSize.width, mPreviewSize.height);

        float hRatio = mScreenW / ((float)cameraWidth);
        float vRatio = mScreenH / ((float)cameraHeight);

        float ratio;
        if(hRatio > vRatio) {
            ratio = mScreenH / (cameraHeight * hRatio);
            mCameraTexCoordBuffer = CameraUtils.createBuffer(new float[]{
                    //UV
                    0f, 0.5f + ratio/2,
                    0f, 0.5f - ratio/2,
                    1f, 0.5f + ratio/2,
                    1f, 0.5f - ratio/2,
            });
        } else {
            ratio = mScreenW/ (cameraWidth * vRatio);
            mCameraTexCoordBuffer = CameraUtils.createBuffer(new float[]{
                    //UV
                    0f, 0.5f + ratio/2,
                    1f, 0.5f + ratio/2,
                    0f, 0.5f - ratio/2,
                    1f, 0.5f - ratio/2,
            });
        }

        mPosBuffer = CameraUtils.createBuffer(new float[]{
                // XYZ
                -1f,  1f, 0f,
                -1f, -1f, 0f,
                1f,   1f, 0f,
                1f,  -1f, 0f,
        });

        mTexBuffer = CameraUtils.createBuffer(new float[]{
                // XYZ
                1f,   1f, 0f,
                1f,  -1f, 0f,
                -1f,  1f, 0f,
                -1f, -1f, 0f,
        });
    }

    private void initAndResetGLProgram() {
        int maPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        int maTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        muPosMtxHandle = GLES20.glGetUniformLocation(mProgram, "uPosMtx");
        muTexMtxHandle = GLES20.glGetUniformLocation(mProgram, "uTexMtx");

        GLES20.glUseProgram(mProgram);

        //设置视口大小
        GLES20.glViewport(0, 0, mScreenW, mScreenH);

        //设置定点坐标
        if(mIrrorImage) {
            mPosBuffer.position(0);
            GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 4 * 3, mPosBuffer);
        } else {
            mTexBuffer.position(0);
            GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 4 * 3, mTexBuffer);
        }

        //设置纹理坐标
        mCameraTexCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(maTexCoordHandle,
                2, GLES20.GL_FLOAT, false, 4 * 2, mCameraTexCoordBuffer);


        // 启用顶点位置的句柄
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoordHandle);
    }

    public void draw(final float[] tex_mtx) {
        if (mScreenW <= 0 || mScreenH <= 0) {
            return;
        }

        CameraUtils.checkGlError("draw_S");

        if (mBoolean) {
            initAndResetGLProgram();
        }

        //设置变换矩阵
        if(muPosMtxHandle >= 0) {
            GLES20.glUniformMatrix4fv(muPosMtxHandle, 1, false, mPosCoordinate, 0);
        }

        if(muTexMtxHandle >= 0) {
            GLES20.glUniformMatrix4fv(muTexMtxHandle, 1, false, tex_mtx, 0);
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        CameraUtils.checkGlError("draw_E");
    }
}
