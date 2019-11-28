package com.b96software.schoolplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        splashImage = findViewById(R.id.splashImage);

        //Create Animation for image
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_animation_blink);
        splashImage.startAnimation(animation);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                }
                catch (InterruptedException e){}

                finally {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                }
            }
        };

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
