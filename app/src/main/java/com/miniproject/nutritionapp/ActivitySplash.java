package com.miniproject.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miniproject.nutritionapp.HomeActivities.ActivityHome;
import com.miniproject.nutritionapp.LoginSignup.ActivityAuthentication;
import com.miniproject.nutritionapp.ProfileFrags.ActivityPostRegistration;

public class ActivitySplash extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Intent intent;

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        topAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_anim);



        Thread myThread = new Thread(){
            @Override
            public void run() {
                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                try {
                    imageView.setAnimation(topAnim);
                    textView.setAnimation(bottomAnim);
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                FirebaseUser user = fAuth.getCurrentUser();
                if (user!=null){
                    String userId = user.getUid();
                    fStore.collection(Keys.USER_COLLECTION)
                            .document(userId)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot ds) {
                                    boolean s = ds.getBoolean (Keys.USER_PRDONE);
                                    if(s){
                                        intent = new Intent(getApplicationContext(), ActivityHome.class);

                                    }else {
                                        intent = new Intent(getApplicationContext(), ActivityPostRegistration.class);
                                    }
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    finish();
                                }
                            });
                }else {
                    intent = new Intent(getApplicationContext(), ActivityAuthentication.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    finish();
                }
            }
        };
        myThread.start();
    }
}