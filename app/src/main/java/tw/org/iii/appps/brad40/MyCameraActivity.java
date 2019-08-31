package tw.org.iii.appps.brad40;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class MyCameraActivity extends AppCompatActivity {
    private SurfaceView svCamera;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private File sdroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);

        sdroot = Environment.getDataDirectory();
        svCamera = findViewById(R.id.sv_camera);
        initCamera();
    }

    private void initCamera(){
        try {
            camera = Camera.open(0);
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }

    public void takPic(View view) {
        Log.v("brad", "debug2");
        camera.takePicture(new Camera.ShutterCallback(){
            @Override
            public void onShutter() {
                Log.v("brad", "shutter");
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                Log.v("brad", "debug1");
                File temp = new File(sdroot, "brad20190831.jpg");
                try {
                    FileOutputStream fout =
                            new FileOutputStream(temp);
                    fout.write(bytes);
                    fout.flush();
                    fout.close();
                    Log.v("brad", "OK");
                }catch (Exception e){
                    Log.v("brad", e.toString());
                }
            }
        }, null);
        Log.v("brad", "debug3");

    }
}
