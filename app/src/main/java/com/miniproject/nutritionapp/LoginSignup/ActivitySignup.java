package com.miniproject.nutritionapp.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miniproject.nutritionapp.ProfileFrags.ActivityPostRegistration;
import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignup extends AppCompatActivity implements Keys {

    TextInputEditText mEmail,mPassword;
    TextInputLayout mTxtEmail,mTxtPassword;
    AppCompatButton mSignupBtn;

    MaterialToolbar toolbar;
    String chatroomId;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Set up toolbar
        setupToolbar();

        //bind all the views
        initiateWithID();

        //initiate firebase classes
        initiateFirebaseClasses();

        //add text change listener
        addTextChangeListner();

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void signUp(){
        String emailStr,passwordStr;
        emailStr = mEmail.getText().toString().trim();
        passwordStr = mPassword.getText().toString().trim();

        if (emailStr.equals("")) {
            mTxtEmail.setError("This field cannot be empty");
            return;
        }
        if (passwordStr.length()<8) {
            mTxtPassword.setError("Password length should be 8 characters atleast");
            return;
        }

        progressDialog = new ProgressDialog(mTxtEmail.getContext(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //send verification email to user
                    //sendVerificationMail();
                    //save user data to firebase
                    saveUserToDatabase(emailStr);

                    Intent intent = new Intent(getApplicationContext(), ActivityPostRegistration.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void addTextChangeListner() {
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEmail.getText().toString().trim().length()>0){
                    mTxtEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mPassword.getText().toString().trim().length()>8){
                    mTxtPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void sendVerificationMail() {
        FirebaseUser fUser = fAuth.getCurrentUser();
        fUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Verification Email has been sent to you", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String emailStr) {

        Map<String,Object> userdata = new HashMap<>();
        userdata.put(USER_AVATAR,"");
        userdata.put(USER_EMAIL,emailStr);
        userdata.put(USER_CHATROOMID,"");
        userdata.put(USER_PRDONE,false);


        fStore.collection(USER_COLLECTION)
                .document(fAuth.getUid())
                .set(userdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Data Added Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initiateFirebaseClasses() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    private void initiateWithID() {
        mEmail = findViewById(R.id.signup_mail);
        mPassword = findViewById(R.id.signup_password);

        mTxtEmail = findViewById(R.id.signup_txt_mail);
        mTxtPassword = findViewById(R.id.signup_txt_password);

        mSignupBtn = findViewById(R.id.signup_btn);
    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
            toolbar = findViewById(R.id.toolbar_signup);
            setSupportActionBar(toolbar);
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