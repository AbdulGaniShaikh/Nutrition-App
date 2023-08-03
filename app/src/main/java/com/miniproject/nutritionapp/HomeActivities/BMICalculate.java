package com.miniproject.nutritionapp.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;

public class BMICalculate extends AppCompatActivity {

    android.widget.Button mCalculateBMI;
    TextView mCurrentAge, mCurrentWeight, mCurrentHeight;
    ImageView mIncrementAge, mDecrementAge, mIncrementWeight, mDecrementWeight;
    SeekBar mSeekbarHeight;
    RelativeLayout mMale, mFemale;

    int intWeight, intAge;
    String mIntProgress = "150", typeOfUser = "0";
    String height, weight, age, gender;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalculate);

        //bind all the views
        initiateWithID();

        setupToolbar();

        //initiate firebase classes
        initiateFirebaseClasses();

        mMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_focus_bg));
                mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_unfocus_bg));
                typeOfUser = "Male";
            }
        });

        mFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_focus_bg));
                mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_unfocus_bg));
                typeOfUser = "Female";
            }
        });

        mSeekbarHeight.setMax(300);
//        mSeekbarHeight.setProgress(150);
        mSeekbarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mIntProgress = String.valueOf(i);
                mCurrentHeight.setText(mIntProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mIncrementAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intAge++;
                mCurrentAge.setText(String.valueOf(intAge));
            }
        });

        mDecrementAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intAge--;
                mCurrentAge.setText(String.valueOf(intAge));
            }
        });

        mIncrementWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intWeight++;
                mCurrentWeight.setText(String.valueOf(intWeight));
            }
        });

        mDecrementWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intWeight--;
                mCurrentWeight.setText(String.valueOf(intWeight));
            }
        });

        mCalculateBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeOfUser.equals("0")) {
                    Toast.makeText(BMICalculate.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (mIntProgress.equals("0")) {
                    Toast.makeText(BMICalculate.this, "Please Select Height", Toast.LENGTH_SHORT).show();
                } else if (intAge == 0 || intAge < 0) {
                    Toast.makeText(BMICalculate.this, "Enter Correct Age", Toast.LENGTH_SHORT).show();
                } else if (intWeight ==0 || intWeight < 0) {
                    Toast.makeText(BMICalculate.this, "Enter Correct Weight", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(BMICalculate.this, BMIResult.class);
                    intent.putExtra("gender", typeOfUser);
                    intent.putExtra("height", mIntProgress);
                    intent.putExtra("weight", String.valueOf(intWeight));
                    intent.putExtra("age", String.valueOf(intAge));
                    startActivity(intent);
                }


            }
        });

    }

    private void initiateFirebaseClasses() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String userId = fAuth.getCurrentUser().getUid();

        fStore.collection(Keys.USER_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        height = String.valueOf(documentSnapshot.getLong(Keys.USER_HEIGHT).intValue());
                        weight = String.valueOf(documentSnapshot.getLong(Keys.USER_WEIGHT).intValue());
                        age = String.valueOf(documentSnapshot.getLong(Keys.USER_DOB).intValue());
                        gender = documentSnapshot.getString(Keys.USER_GENDER);

                        mCurrentHeight.setText(height);
                        mCurrentWeight.setText(weight);
                        mCurrentAge.setText(age);

                        mSeekbarHeight.setProgress(Integer.parseInt(height));
                        intWeight = Integer.parseInt(weight);
                        intAge = Integer.parseInt(age);

                        if (gender.equals("male")) {
                            mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_focus_bg));
                            mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_unfocus_bg));
                            typeOfUser = "Male";
                        } else {
                            mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_focus_bg));
                            mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_unfocus_bg));
                            typeOfUser = "Female";
                        }
                    }
                });
    }

    private void initiateWithID() {
        mCalculateBMI = findViewById(R.id.calculate_bmi);

        mCurrentAge = findViewById(R.id.currentAge);
        mCurrentWeight = findViewById(R.id.currentWeight);
        mCurrentHeight = findViewById(R.id.currentHeight);

        mIncrementAge = findViewById(R.id.increment_age);
        mDecrementAge = findViewById(R.id.decrement_age);
        mIncrementWeight = findViewById(R.id.increment_weight);
        mDecrementWeight = findViewById(R.id.decrement_weight);

        mSeekbarHeight = findViewById(R.id.seekbarheight);

        mMale = findViewById(R.id.male);
        mFemale = findViewById(R.id.female);

        toolbar = findViewById(R.id.toolbar_calbmi);
    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle(" ");
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