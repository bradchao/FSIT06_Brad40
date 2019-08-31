package tw.org.iii.appps.brad40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import android.os.Bundle;
import android.view.View;

public class MyCameraXActivity extends AppCompatActivity
    implements LifecycleOwner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera_x);
    }

    public void takePic2(View view) {
    }

    @Override
    public void finish() {
        super.finish();
    }
}
