package com.miniproject.nutritionapp.HomeActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.LoginSignup.ActivityAuthentication;
import com.miniproject.nutritionapp.R;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityProfile extends AppCompatActivity implements Keys {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    StorageReference storageReference;

    Uri imgUri;
    ProgressDialog progressDialog;

    String userId;
    String name,mail,lgender,gender,age,height,weight;

    private final int REQUEST_CODE = 100;

    TextView mName,mMail,mBMI;
    AppCompatButton mUploadImage,mLogout,mMale,mFemale;
    EditText mAge,mHeight,mWeight;
    ShapeableImageView mImageView;
    LinearLayout mBMILL;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //bind all the views
        initiateWithID();

        //initiate firebase classes
        initiateFirebaseClasses();

        //setup toolbar
        setupToolbar();

        //get data from firebase and assign values to the variables
        getDatafromFirebase();


        mMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lgender = USER_MALE;
                mMale.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                mFemale.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        mFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lgender = USER_FEMALE;
                mFemale.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                mMale.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), ActivityAuthentication.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    private void getDatafromFirebase() {

        fStore.collection(USER_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        name = ds.getString(USER_NAME);
                        mail = ds.getString(USER_EMAIL);
                        gender = ds.getString(USER_GENDER);
                        lgender = gender;


                        age = ds.get(USER_DOB).toString();
                        height = ds.get(USER_HEIGHT).toString();
                        weight = ds.get(USER_WEIGHT).toString();

                        mName.setText(name);
                        mMail.setText(mail);

                        mAge.setText(age);
                        mHeight.setText(height);
                        mWeight.setText(weight);

                        float floatHeight = Float.parseFloat(height);
                        float floatWeight = Float.parseFloat(weight);

                        floatHeight = floatHeight/100;

                        float floatBMI = floatWeight/(floatHeight*floatHeight);
                        mBMI.setText(String.format("%.2f", floatBMI));

                        if (gender.equals(USER_MALE)) {
                            mMale.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                            mFemale.setBackgroundColor(Color.TRANSPARENT);
                        } else {
                            mFemale.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                            mMale.setBackgroundColor(Color.TRANSPARENT);
                        }

                        String imgname = ds.getString(Keys.USER_AVATAR);
                            if(imgname.length()>0) {
                                try {
                                    final File file = File.createTempFile(imgname, "");
                                    storageReference.child(USER_COLLECTION + "/" + imgname)
                                            .getFile(file)
                                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    mImageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                                                }
                                            });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    }
                });

    }

    private void initiateWithID() {

        mName = findViewById(R.id.username_profile);
        mMail = findViewById(R.id.mail_profile);
        mBMI = findViewById(R.id.bmi_profile);

        mUploadImage = findViewById(R.id.upload_profile);
        mMale = findViewById(R.id.male_profile);
        mFemale = findViewById(R.id.female_profile);
        mLogout = findViewById(R.id.logout_profile);

        mAge = findViewById(R.id.age_profile);
        mHeight = findViewById(R.id.height_profile);
        mWeight = findViewById(R.id.weight_profile);

        mImageView = findViewById(R.id.avatar_profile);
        mBMILL = findViewById(R.id.bmill_profile);

    }

    private void initiateFirebaseClasses() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        storageReference = fStorage.getReference();

        userId = fAuth.getCurrentUser().getUid();
    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
            toolbar = findViewById(R.id.toolbar_profile);
            toolbar.setTitle(" ");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if(item.getItemId()==R.id.menu_update){
            updateData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateData() {

        int lage,lheight, lweight;

        if (!checkIfValuesChanged()){
            Toast.makeText(getApplicationContext(), "No changes were made.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAge.getText().toString().trim().equals("")){
            mAge.setError("Enter Age");
            return;
        }
        if (mHeight.getText().toString().trim().equals("")){
            mHeight.setError("Enter Height");
            return;
        }

        lage = Integer.parseInt(mAge.getText().toString().trim());
        lheight = Integer.parseInt(mHeight.getText().toString().trim());
        lweight = Integer.parseInt(mWeight.getText().toString().trim());

        if (lage<=10 || lage>=120){
            mAge.setError("Enter Age between 10-120");
            return;
        }
        if (lheight<=20 || lheight>=300){
            mHeight.setError("Enter Height between 20-300");
            return;
        }
        if (lheight<=10 || lheight>=200){
            mWeight.setError("Enter Weight between 10-200");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(USER_DOB,lage);
        map.put(USER_GENDER,lgender);
        map.put(USER_HEIGHT,lheight);
        map.put(USER_WEIGHT,lweight);

        fStore.collection(USER_COLLECTION)
                .document(userId)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
                        getDatafromFirebase();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==REQUEST_CODE){
            imgUri = data.getData();
            mImageView.setImageURI(imgUri);
            uploadImage();
        }
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String imgName = UUID.randomUUID().toString();
        storageReference.child(USER_COLLECTION+"/"+imgName)
                .putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Map<String, Object> data = new HashMap<>();
                        data.put(USER_AVATAR,imgName);

                        fStore.collection(USER_COLLECTION)
                                .document(userId)
                                .update(data);
                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

    }

    public boolean checkIfValuesChanged(){
        return !lgender.equals(gender) ||
                !age.equals(mAge.getText().toString().trim()) ||
                !height.equals(mHeight.getText().toString().trim()) ||
                !weight.equals(mWeight.getText().toString().trim());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }
}