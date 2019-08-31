package tw.org.iii.appps.brad40;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    private Vibrator vibrator;
    private SwitchCompat fswitch;
    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    12);
        }else{
            init();
        }


        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        fswitch = findViewById(R.id.fswitch);
        fswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.v("brad", "b = " + b);
                if (b){
                    onFlashLight();
                }else{
                    offFlashLight();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

    }

    private void onFlashLight(){
        try {
            cameraManager.setTorchMode("0", true);
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }
    private void offFlashLight(){
        try {
            cameraManager.setTorchMode("0", false);
        }catch (Exception e){
            Log.v("brad", e.toString());
        }
    }


    public void test1(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(
                    VibrationEffect.createOneShot(1*1000,
                            VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            vibrator.vibrate(1*1000);
        }
    }
    public void test2(View view) {
        long[] pattern = {0, 3*1000, 1*1000,
                        3*1000, 1*1000,
                        3*1000, 1*1000, };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 3));
            vibrator.vibrate(pattern, -1);
        }else{
            vibrator.vibrate(pattern, 0);
        }
    }
}
