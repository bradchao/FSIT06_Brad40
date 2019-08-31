package tw.org.iii.appps.brad40;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Vibrator vibrator;
    private SwitchCompat fswitch;
    private CameraManager cameraManager;
    private ImageView img;
    private File sdroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    12);
        }else{
            init();
        }

        img = findViewById(R.id.img);
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
        sdroot = Environment.getExternalStorageDirectory();
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

    public void test3(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 3);
    }

    public void test4(View view){

        Uri photoURI = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                new File(sdroot, "iii.jpg"));

//        Uri uriFile = Uri.fromFile(
//                new File(sdroot.getAbsoluteFile()+"/iii2.jpg"));


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, 4);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            if (requestCode == 3){
                Bundle bundle = data.getExtras();
                Set<String> keys = bundle.keySet();
                for (String key: keys){
                    Log.v("brad", "key = " + key);
                    Object obj = bundle.get(key);
                    Log.v("brad", obj.getClass().getName());
                }
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bmp);
            }else if (requestCode == 4){

                Bitmap bmp = BitmapFactory.decodeFile(
                        sdroot.getAbsolutePath()+"/iii.jpg");
                img.setImageBitmap(bmp);


//                Uri photoURI = FileProvider.getUriForFile(
//                        this,
//                        getPackageName() + ".provider",
//                        new File(sdroot, "iii.jpg"));
//                img.setImageURI(photoURI);
            }
        }
    }

    public void test5(View view) {
        Intent intent = new Intent(this, MyCameraActivity.class);
        startActivity(intent);
    }
}
