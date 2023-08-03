package com.miniproject.nutritionapp.HomeActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityChat extends AppCompatActivity implements Keys {

    RecyclerView recyclerView;
    AdapterChat adapter;
    List<ModelMessage> list;

    List<String> mess;

    String userId,chatId,recieverId;

    MaterialToolbar toolbar;
    ImageButton send;
    EditText message;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edtMessage =message.getText().toString().trim();
                if (message.equals("")){
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                map.put(MESSAGE,edtMessage);
                map.put(MESSAGE_TIME, new Timestamp(new Date()));
                map.put(MESSAGE_SUSERID,userId);
                map.put(MESSAGE_RUSERID,recieverId);

                fStore.collection(MESSAGE_COLLECTION)
                        .add(map)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()){
                                    fStore.collection(CHATROOM_COLLECTION)
                                            .document(chatId)
                                            .update(CHATROOM_MESSAGES,FieldValue.arrayUnion(task.getResult().getId()));
                                    message.setText("");
                                }
                            }
                        });
            }
        });

    }

    private void initiateWithID() {

        send = findViewById(R.id.send_chat);
        message = findViewById(R.id.message_chat);

        recyclerView = findViewById(R.id.rv_chat);
        toolbar = findViewById(R.id.toolbar_chat);

    }

    private void initiateFirebaseClasses() {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        chatId = intent.getStringExtra(USER_CHATROOMID);
        recieverId = intent.getStringExtra(MESSAGE_RUSERID);

    }

    private void getDatafromFirebase() {

        fStore.collection(CHATROOM_COLLECTION)
                .document(chatId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        if (ADMIN_ID.equals(userId))
                            toolbar.setTitle(value.getString(CHATROOM_NAME));
                        else
                            toolbar.setTitle("Admin");

                        mess = (List<String>) value.get(CHATROOM_MESSAGES);

//                        for(String s : mess){
//                            if(s.equals("0")){
//                                continue;
//                            }
                        fStore.collection(MESSAGE_COLLECTION)
                                .orderBy(MESSAGE_TIME)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        list.clear();
                                        adapter.notifyDataSetChanged();
                                        List<DocumentSnapshot> res = queryDocumentSnapshots.getDocuments();

                                        for (DocumentSnapshot ds : res){
                                            for(int i=1;i<mess.size();i++){
                                                if (mess.get(i).equals(ds.getId())){
                                                    mess.remove(i);

                                                    String message = ds.getString(MESSAGE);
                                                    Timestamp timestamp = (Timestamp)ds.get(MESSAGE_TIME);
                                                    Date date =  timestamp.toDate();
                                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                                    String time = sdf.format(date);
                                                    boolean isSender = ds.getString(MESSAGE_SUSERID).equals(userId);
                                                    list.add(new ModelMessage(message,time,isSender));

                                                    adapter.notifyItemInserted(list.size()-1);
                                                }
                                            }
                                        }
                                    }
                                });
                                    /*.document(s)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot ds) {
                                            String message = ds.getString(MESSAGE);
                                            Timestamp timestamp = (Timestamp)ds.get(MESSAGE_TIME);
                                            Date date =  timestamp.toDate();
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                            String time = sdf.format(date);
                                            boolean isSender = ds.getString(MESSAGE_SUSERID).equals(userId);
                                            list.add(new ModelMessage(message,time,isSender));

                                            adapter.notifyItemInserted(list.size()-1);
                                        }
                                    });
                        }*/
                    }
                });

    }

    private void setupRecyclerView() {

        list = new ArrayList<>();

        adapter = new AdapterChat(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


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