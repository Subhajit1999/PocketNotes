package com.subhajitkar.commercial.projet_tulip.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.subhajitkar.commercial.projet_tulip.R;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private final static int SPLASH_TIME_OUT = 2000;

    ImageView splashLogo, splashName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Inside of Splash activity onCreate");
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //imageViews with animation
        splashLogo = findViewById(R.id.splash_logo);
        splashName = findViewById(R.id.splash_name);
        //Animations
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_bottom);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_top);
        splashLogo.setAnimation(topAnim);
        splashName.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
