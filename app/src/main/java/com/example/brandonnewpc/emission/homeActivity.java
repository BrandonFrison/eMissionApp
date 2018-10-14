package com.example.brandonnewpc.emission;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class homeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void swapToCalculator(View view){
        startActivity(new Intent(homeActivity.this, FoodSurveyActivity.class));
    }

    public void swapToAboutPage(View view){
        startActivity(new Intent(homeActivity.this, AboutPage.class));
    }
}
