package com.miniproject.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.miniproject.nutritionapp.LoginSignup.ActivityAuthentication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent;
        intent = new Intent(getApplicationContext(), ActivityAuthentication.class);
        startActivity(intent);
    }
}