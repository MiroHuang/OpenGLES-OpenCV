package com.example.testjnidemo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testjnidemo.Utils.CameraController;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = "MainActivity";
    private GLSurfaceView mGLSurfaceView;
    private static int REQUEST_EXTERNAL_STORAGE = 1;
    private static String []PERMISSIONS_STORAGE = new String[]{
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    public TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        initView();
    }

    private void initView() {
        try {
            mTextView = findViewById(R.id.textView);
            mGLSurfaceView = (GLSurfaceView) findViewById(R.id.camera_glsurface_view);
            CameraController.getInstance().openCamera(MainActivity.this, mGLSurfaceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btn_gl_surface_view_animator(View view) {
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.5f,1.0f);
        PropertyValuesHolder valuesHolder4 = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.5f,1.0f);
        PropertyValuesHolder valuesHolder5 = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 360.0f, 0.0F);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mGLSurfaceView, valuesHolder1, valuesHolder4,valuesHolder5);
        objectAnimator.setDuration(3000).start();
    }

    public void btn_gl_surface_view_switch(View view) {
        CameraController.getInstance().switchCamera();
    }

    private void checkPermission() {
        try {
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
