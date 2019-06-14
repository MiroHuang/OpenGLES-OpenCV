package com.example.testjnidemo;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.IOException;
import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = CameraManager.class.getName();
    private ArrayList<ObjectDetector> mObjectDetects;
    private ObjectDetector mFaceDetector;
    private Mat grayscaleImage;
    private float absoluteFaceSize = 2.2F;
    private SurfaceView mGLSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private CameraManager cameraManager;
    private CustomImageButton cimbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        cimbt = (CustomImageButton) findViewById(R.id.imbt_bitmap);
        cimbt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.e("log_wons", "OpenCV init error");
        }

        mObjectDetects = new ArrayList<>();
        mFaceDetector = new ObjectDetector(this, R.raw.haarcascade_frontalface_alt, 6, absoluteFaceSize, absoluteFaceSize, new Scalar(255, 0, 0, 255));
        mObjectDetects.add(mFaceDetector);

        mGLSurfaceView = (SurfaceView) findViewById(R.id.camera_glsurface_view);
        mSurfaceHolder = mGLSurfaceView.getHolder();

        // mSurfaceView 不需要自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // mSurfaceView添加回调
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //SurfaceView创建
                try {
                    cameraManager = new CameraManager(Main3Activity.this, mObjectDetects, cimbt, mSurfaceHolder);
                    cameraManager.openDriver();
                    cameraManager.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView销毁
                holder.removeCallback(this); // Camera is being used after Camera.release() was called
                cameraManager.stopPreview();
                cameraManager.closeDriver();

            }
        });
    }

    /**
     * 点击事件
     *
     * @param v
     */
    public void changeClick(View v) throws IOException {

        int bt_id = v.getId();
        switch (bt_id) {
            case R.id.changeText:
                cameraManager.changeCamera();
                break;

            default:
                break;
        }


    }
}
