package tw.org.iii.appps.brad40;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

public class MyCameraXActivity extends AppCompatActivity
    implements LifecycleOwner {

    private ImageCapture imageCapture;
    private TextureView textureView;
    private ViewGroup viewGroup;

    private LifecycleRegistry mLifecycleRegistry;
    private File sdroot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera_x);

        sdroot = Environment.getExternalStorageDirectory();

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        viewGroup = findViewById(R.id.viewGroup);
        textureView = findViewById(R.id.textureView);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
    }

    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    private void init(){
        textureView.post(new Runnable() {
            @Override
            public void run() {
                startCamera();
            }
        });

        textureView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                updateTransorm();
            }
        });
    }

    private void updateTransorm(){
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(0, w, h);
        textureView.setTransform(matrix);
    }



    private void startCamera(){
        CameraX.unbindAll();

        Rational rate = new Rational(textureView.getWidth(), textureView.getHeight());
        Size size = new Size(textureView.getWidth(), textureView.getHeight());

        PreviewConfig config = new PreviewConfig.Builder()
                .setTargetAspectRatio(rate)
                .setTargetResolution(size)
                .build();


        //PreviewConfig config = new PreviewConfig.Builder().build();
        Preview preview = new Preview(config);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                //textureView.setSurfaceTexture(output.getSurfaceTexture());

                ViewGroup parent = (ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView,0);

                textureView.setSurfaceTexture(output.getSurfaceTexture());
                updateTransorm();

            }
        });

        ImageCaptureConfig imageCaptureConfig =
                new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();
        imageCapture = new ImageCapture(imageCaptureConfig);

//        ImageCaptureConfig capconfig =
//                new ImageCaptureConfig.Builder()
//                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
//                        .build();
//        imageCapture = new ImageCapture(capconfig);

        //CameraX.bindToLifecycle((LifecycleOwner) this, imageCapture, imageAnalysis, preview);
        CameraX.bindToLifecycle(this, preview, imageCapture);

    }


    public void takePic2(View view) {

        File file = new File(sdroot, "iii20190831.jpg");
        imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
            @Override
            public void onImageSaved(@NonNull File file) {
                Log.v("brad", "OK");
            }

            @Override
            public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                Log.v("brad", "XX");
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
    }
}
