package com.example.brandonnewpc.emission;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    private int SPLASH_TIMER = 1; //number of seconds splash screen is on for
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //makes the splash screen fullscreen basically removes top bar
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide(); //hides action bar as well for max fullscreen

        SplashLauncher splashLauncher = new SplashLauncher();
        splashLauncher.start();
    }


    private class SplashLauncher extends Thread{
        public void run(){
            try{
                sleep(1000 * SPLASH_TIMER);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent(SplashScreenActivity.this, homeActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish(); //kills the activity so it doesn't lag app
        }
    }
}
