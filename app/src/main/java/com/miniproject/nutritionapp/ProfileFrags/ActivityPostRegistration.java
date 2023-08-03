package com.miniproject.nutritionapp.ProfileFrags;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miniproject.nutritionapp.HomeActivities.ActivityHome;
import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityPostRegistration extends AppCompatActivity implements Keys {

    Button next, back;
    SharedPreferences sp;

    String userId,chatroomId;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);

        //bind all the views
        initiateWithID();

        //initiate firebase classes
        initiateFirebaseClasses();

        replaceFragement(new FragmentName(),true);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean nextb = true;
                sp = getSharedPreferences( USER_NAME, Context.MODE_PRIVATE);
                Object object = fragmentManager.findFragmentById(R.id.framelayout_pr);

                String name,gender;
                int age,height, weight;
                name = sp.getString( USER_NAME,"");
                age = sp.getInt( USER_DOB,-1);
                gender = sp.getString( USER_GENDER,"");
                weight = sp.getInt( USER_WEIGHT,-1);
                height = sp.getInt( USER_HEIGHT,-1);

                if(object instanceof FragmentName && !name.equals("")){
                    replaceFragement(new FragmentAge(),nextb);

                }else if(object instanceof FragmentAge  && age!=-1){
                    replaceFragement(new FragmentGender(),nextb);

                }else if(object instanceof FragmentGender  && !gender.equals("")){
                    replaceFragement(new FragmentWeight(),nextb);

                }else if(object instanceof FragmentWeight  && weight!=-1){
                    replaceFragement(new FragmentHeight(),nextb);

                }else if(object instanceof FragmentHeight  && age!=-1){
                    savetoDatabase(name,age,gender,height,weight);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean backb = false;
                Object object = fragmentManager.findFragmentById(R.id.framelayout_pr);

                if(object instanceof FragmentAge){
                    replaceFragement(new FragmentName(),backb);

                }else if(object instanceof FragmentGender){
                    replaceFragement(new FragmentAge(),backb);

                }else if(object instanceof FragmentWeight ){
                    replaceFragement(new FragmentGender(),backb);

                }else if(object instanceof FragmentHeight ){
                    replaceFragement(new FragmentWeight(),backb);
                }
            }
        });



    }

    private void savetoDatabase(String name, int age, String gender, int height, int weight) {
        Map<String, Object> map =  new HashMap<>();
        map.put(USER_NAME,name);
        map.put(USER_DOB,age);
        map.put(USER_GENDER,gender);
        map.put(USER_HEIGHT,height);
        map.put(USER_WEIGHT,weight);
        map.put(USER_PRDONE,true);
        chatroomId = UUID.randomUUID().toString();
        map.put(USER_CHATROOMID,chatroomId);

        fStore.collection(USER_COLLECTION)
                .document(userId)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String, Object> map1 = new HashMap<>();
                        map1.put(CHATROOM_MESSAGES, Arrays.asList("0"));
                        map1.put(CHATROOM_NAME,name);
                        map1.put(CHATROOM_USERID,userId);

                        fStore.collection(CHATROOM_COLLECTION)
                                .document(chatroomId)
                                .set(map1);

                        Intent intent = new Intent(ActivityPostRegistration.this, ActivityHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initiateFirebaseClasses() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
    }

    private void initiateWithID() {

        next = findViewById(R.id.next_pr);
        back = findViewById(R.id.back_pr);

        fragmentManager = getSupportFragmentManager();

    }

    private void replaceFragement(Fragment fragment,boolean next) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(next) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        }else{
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_alter, R.anim.slide_out_alter);
        }
        fragmentTransaction.replace(R.id.framelayout_pr, fragment);
        fragmentTransaction.commit();
    }
}