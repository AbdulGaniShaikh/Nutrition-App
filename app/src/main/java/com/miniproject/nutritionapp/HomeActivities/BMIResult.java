package com.miniproject.nutritionapp.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.miniproject.nutritionapp.R;

public class BMIResult extends AppCompatActivity {

    android.widget.Button mViewDietPlan;

    MaterialToolbar toolbar;

    TextView mResultBMI, mResultGender, mResultCategory;
    ImageView mResultImage;
    Intent intent;
    String mBMI, height, weight, gender;
    float floatBMI, floatHeight, floatWeight;
    RelativeLayout mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiresult);

        //bind all the views
        initiateWithID();

        //setup toolbar
        setupToolbar();

        intent = getIntent();

        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");
        gender = intent.getStringExtra("gender");

        floatHeight = Float.parseFloat(height);
        floatWeight = Float.parseFloat(weight);

        floatHeight = floatHeight/100;

        floatBMI = floatWeight/(floatHeight*floatHeight);
        mBMI = String.valueOf(floatBMI);

        if (gender.equals("Male")){
            if (floatBMI < 16) {
                mResultCategory.setText("Underweight");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                mResultImage.setImageResource(R.drawable.result_bad);
                mResultBMI.setTextColor(Color.parseColor("#ffffff"));
                mResultGender.setTextColor(Color.parseColor("#ffffff"));
                mResultCategory.setTextColor(Color.parseColor("#ffffff"));
            } else if (floatBMI > 16 && floatBMI <= 18.5) {
                mResultCategory.setText("Underweight");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                mResultImage.setImageResource(R.drawable.result_warning);
            } else if (floatBMI > 18.5 && floatBMI <= 25) {
                mResultCategory.setText("Normal");
//                mBackground.setBackgroundColor(Color.GREEN);
                mResultImage.setImageResource(R.drawable.result_good);
            } else if (floatBMI > 25 && floatBMI <= 35) {
                mResultCategory.setText("Overweight");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                mResultImage.setImageResource(R.drawable.result_warning);
            } else if (floatBMI > 35) {
                mResultCategory.setText("Obesity");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                mResultImage.setImageResource(R.drawable.result_bad);
                mResultBMI.setTextColor(Color.parseColor("#ffffff"));
                mResultGender.setTextColor(Color.parseColor("#ffffff"));
                mResultCategory.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            if (floatBMI < 16) {
                mResultCategory.setText("Underweight");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                mResultImage.setImageResource(R.drawable.result_bad);
                mResultBMI.setTextColor(Color.parseColor("#ffffff"));
                mResultGender.setTextColor(Color.parseColor("#ffffff"));
                mResultCategory.setTextColor(Color.parseColor("#ffffff"));
            } else if (floatBMI > 16 && floatBMI <= 18.5) {
                mResultCategory.setText("Underweight");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                mResultImage.setImageResource(R.drawable.result_warning);
            } else if (floatBMI > 18.5 && floatBMI <= 24.9) {
                mResultCategory.setText("Normal");
//                mBackground.setBackgroundColor(Color.GREEN);
                mResultImage.setImageResource(R.drawable.result_good);
            } else if (floatBMI > 25 && floatBMI <= 29) {
                mResultCategory.setText("Overweight");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                mResultImage.setImageResource(R.drawable.result_warning);
            } else if (floatBMI > 29) {
                mResultCategory.setText("Obesity");
                mBackground.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                mResultImage.setImageResource(R.drawable.result_bad);
                mResultBMI.setTextColor(Color.parseColor("#ffffff"));
                mResultGender.setTextColor(Color.parseColor("#ffffff"));
                mResultCategory.setTextColor(Color.parseColor("#ffffff"));
            }
        }

        mResultGender.setText(gender);
        mResultBMI.setText(String.format("%.2f", floatBMI));

        mViewDietPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(BMIResult.this, ));
            }
        });

    }

    private void initiateWithID() {
        mViewDietPlan = findViewById(R.id.result_diet_plan);

        mResultBMI = findViewById(R.id.result_bmi);
        mResultGender = findViewById(R.id.result_gender);
        mResultCategory = findViewById(R.id.result_category);
        mResultImage = findViewById(R.id.result_img);
        mBackground = findViewById(R.id.contentLayout);

        toolbar = findViewById(R.id.toolbar_mbires);
    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            toolbar.setTitle(" ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}