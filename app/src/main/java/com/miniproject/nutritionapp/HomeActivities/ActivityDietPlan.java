package com.miniproject.nutritionapp.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;

import java.util.Calendar;

public class ActivityDietPlan extends AppCompatActivity implements View.OnClickListener, Keys {

    TextView mSun, mMon, mTue, mWed, mThu, mFri, mSat, mDay, mBMI;
    FragmentManager fragmentManager;
    Fragment fragmentSun, fragmentMon, fragmentTue, fragmentWed, fragmentThu, fragmentFri, fragmentSat;
    int position;

    MaterialToolbar toolbar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, height, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        //bind all the views
        initiateWithID();

        //setup toolbar
        setupToolbar();

        //initiate firebase classes
        initiateFirebaseClasses();

        //get data from firebase and assign values to the variables
        getDatafromFirebase();


        mSun.setOnClickListener(this);
        mMon.setOnClickListener(this);
        mTue.setOnClickListener(this);
        mWed.setOnClickListener(this);
        mThu.setOnClickListener(this);
        mFri.setOnClickListener(this);
        mSat.setOnClickListener(this);

    }

    private void initiateWithID() {

        mSun = findViewById(R.id.diet_sun);
        mMon = findViewById(R.id.diet_mon);
        mTue = findViewById(R.id.diet_tue);
        mWed = findViewById(R.id.diet_wed);
        mThu = findViewById(R.id.diet_thu);
        mFri = findViewById(R.id.diet_fri);
        mSat = findViewById(R.id.diet_sat);

        mDay = findViewById(R.id.diet_day);
        mBMI = findViewById(R.id.bmi_dietplan);

        toolbar = findViewById(R.id.toolbar_dietplan);

        fragmentManager = getSupportFragmentManager();

        String[] diet = {"A", "B", "C", "D", "E"};
        String[] diet1 = {"A1", "B1", "C1", "D1", "E1"};

        fragmentSun = new FragmentDiet(diet);
        fragmentMon = new FragmentDiet(diet1);
        fragmentTue = new FragmentDiet(diet);
        fragmentWed = new FragmentDiet(diet);
        fragmentThu = new FragmentDiet(diet);
        fragmentFri = new FragmentDiet(diet);
        fragmentSat = new FragmentDiet(diet);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                replaceDiet(mSun, "Sunday", fragmentSun, 1);
                break;
            case Calendar.MONDAY:
                replaceDiet(mMon, "Monday", fragmentMon, 2);
                break;
            case Calendar.TUESDAY:
                replaceDiet(mTue, "Tuesday", fragmentTue, 3);
                break;
            case Calendar.WEDNESDAY:
                replaceDiet(mWed, "Wednesday", fragmentWed, 4);
                break;
            case Calendar.THURSDAY:
                replaceDiet(mThu, "Thursday", fragmentThu, 5);
                break;
            case Calendar.FRIDAY:
                replaceDiet(mFri, "Friday", fragmentFri, 6);
                break;
            case Calendar.SATURDAY:
                replaceDiet(mSat, "Saturday", fragmentSat, 7);
                break;
        }
    }

    private void initiateFirebaseClasses() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
    }

    private void getDatafromFirebase() {

        fStore.collection(USER_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {

                        height = ds.get(USER_HEIGHT).toString();
                        weight = ds.get(USER_WEIGHT).toString();

                        float floatHeight = Float.parseFloat(height);
                        float floatWeight = Float.parseFloat(weight);

                        floatHeight = floatHeight/100;

                        float floatBMI = floatWeight/(floatHeight*floatHeight);
                        mBMI.setText("BMI: " + String.format("%.2f", floatBMI));

                    }
                });

    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle("Diet Plan");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diet_sun:
                position = replaceDiet(mSun, "Sunday", fragmentSun, 1);
//                Toast.makeText(this, "Sun", Toast.LENGTH_SHORT).show();
                break;
            case R.id.diet_mon:
                position = replaceDiet(mMon, "Monday", fragmentMon, 2);
//                Toast.makeText(this, "Mon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.diet_tue:
                position = replaceDiet(mTue, "Tuesday", fragmentTue, 3);
//                Toast.makeText(this, "Tue", Toast.LENGTH_SHORT).show();
                break;
            case R.id.diet_wed:
                position = replaceDiet(mWed, "Wednesday", fragmentWed, 4);
//                Toast.makeText(this, "Wed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.diet_thu:
                position = replaceDiet(mThu, "Thursday", fragmentThu, 5);
//                Toast.makeText(this, "Thu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.diet_fri:
                position = replaceDiet(mFri, "Friday", fragmentFri, 6);
//                Toast.makeText(this, "Fri", Toast.LENGTH_SHORT).show();
                break;
            case R.id.diet_sat:
                position = replaceDiet(mSat, "Saturday", fragmentSat, 7);
//                Toast.makeText(this, "Sat", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int replaceDiet(TextView textView, String day, Fragment fragment, int pos) {
        mSun.setBackground(null);
        mMon.setBackground(null);
        mTue.setBackground(null);
        mWed.setBackground(null);
        mThu.setBackground(null);
        mFri.setBackground(null);
        mSat.setBackground(null);

        textView.setBackground(getResources().getDrawable(R.drawable.diet_plan_days));

        mDay.setText(day);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (pos < position) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_alter, R.anim.slide_out_alter);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        }
        fragmentTransaction.replace(R.id.framelayout_diet, fragment);
        fragmentTransaction.commit();

        return pos;
    }
}