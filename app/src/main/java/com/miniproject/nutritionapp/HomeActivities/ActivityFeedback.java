package com.miniproject.nutritionapp.HomeActivities;

import static com.miniproject.nutritionapp.Keys.FEEDBACK;
import static com.miniproject.nutritionapp.Keys.FEEDBACK_COLLECTION;
import static com.miniproject.nutritionapp.Keys.FEEDBACK_RATING;
import static com.miniproject.nutritionapp.Keys.FEEDBACK_USERID;
import static com.miniproject.nutritionapp.Keys.USER_COLLECTION;
import static com.miniproject.nutritionapp.Keys.USER_FEEDBACK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;
import com.miniproject.nutritionapp.R;

import java.util.HashMap;
import java.util.Map;

public class ActivityFeedback extends AppCompatActivity {

    TextView textView;
    MaterialToolbar toolbar;
    AppCompatButton mSubmitFeedback;
    TextInputEditText mFeedback;
    SmileyRating smileRating;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String rating,userId;
    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //bind all the views
        initiateWithID();

        setupToolbar();

        //initiate firebase classes
        initiateFirebaseClasses();

        String text = "Drop us Feedback!";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.primaryColor));
        ss.setSpan(foregroundColorSpan, 8, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);

        mSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });

    }

    private void initiateWithID() {
        textView = findViewById(R.id.feedback_text1);
        toolbar = findViewById(R.id.toolbar_feedback);

        mSubmitFeedback = findViewById(R.id.submit_feedback);
        mFeedback = findViewById(R.id.feedback);
        smileRating = findViewById(R.id.smile_rating);
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

    private void initiateFirebaseClasses() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
    }

    private void submitFeedback() {

        String feedback = mFeedback.getText().toString().trim();
        rate = smileRating.getSelectedSmiley().getRating();

        if (rate<=0){
            Toast.makeText(getApplicationContext(), "Set rating first", Toast.LENGTH_SHORT).show();
            return;
        }
        if(feedback.equals("")){
            Toast.makeText(getApplicationContext(), "Enter feedback message", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,Object> map = new HashMap<>();

        map.put(FEEDBACK,feedback);
        map.put(FEEDBACK_RATING,rate);
        map.put(FEEDBACK_USERID,userId);

        fStore.collection(FEEDBACK_COLLECTION)
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){

                            Map<String, Object> map1 = new HashMap<>();
                            map1.put(USER_FEEDBACK,task.getResult().getId());

                            fStore.collection(USER_COLLECTION)
                                    .document(userId)
                                    .update(map1);

                            mFeedback.setText("");
                            Toast.makeText(getApplicationContext(), "Thank you for the feedback", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

}