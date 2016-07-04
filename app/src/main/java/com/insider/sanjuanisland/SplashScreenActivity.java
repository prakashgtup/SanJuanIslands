package com.insider.sanjuanisland;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


public class SplashScreenActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Hide Action bar
       // getSupportActionBar().hide();
        try {
            handler.postDelayed(splashrunnable, 3000);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    Runnable splashrunnable =new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(SplashScreenActivity.this,
                    HomeScreenActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeCallbacks(splashrunnable);
    }
}
