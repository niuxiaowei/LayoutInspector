package com.example.layoutinspector;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Main";
    private MyView myView;
    private MediaProjectionManager mMediaProjectionManager;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onBackPressed() {
        mainFragment.onBackPressed();
    }
}