package com.miniproject.nutritionapp.AdminActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniproject.nutritionapp.HomeActivities.ActivityChat;
import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdminChat extends AppCompatActivity implements Keys {

    AdapterAdminChat adapter;
    List<ModelAdminChat> list;
    RecyclerView recyclerView;

    MaterialToolbar toolbar;
    String userId;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

        //bind all the views
        initiateWithID();

        //initiate firebase classes
        initiateFirebaseClasses();

        //setup toolbar
        setupToolbar();

        //setup recycler view
        setupRecyclerView();

        //get data from the firebase
        getDatafromFirebase();

    }

    private void initiateWithID() {

        recyclerView = findViewById(R.id.rv_adminchat);
        toolbar = findViewById(R.id.toolbar_adminchat);

    }

    private void initiateFirebaseClasses() {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

    }

    private void getDatafromFirebase() {

        fStore.collection(CHATROOM_COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        for(DocumentSnapshot ds : value.getDocuments()){
                            list.add(new ModelAdminChat(ds.getId(), ds.getString(CHATROOM_NAME),ds.getString(CHATROOM_USERID)));
                            adapter.notifyItemInserted(list.size()-1);
                        }
                    }
                });

    }

    private void setupRecyclerView() {

        list = new ArrayList<>();

        adapter = new AdapterAdminChat(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterAdminChat.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getApplicationContext(), ActivityChat.class);
                intent.putExtra(USER_CHATROOMID,list.get(position).getChatId());
                intent.putExtra(MESSAGE_RUSERID,list.get(position).getRecieverId());
                startActivity(intent);
            }
        });
    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
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