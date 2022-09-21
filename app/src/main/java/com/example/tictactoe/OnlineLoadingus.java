package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class OnlineLoadingus extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference roomRef;
    ValueEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_loadingus);


        database = FirebaseDatabase.getInstance("https://tictactoe-aa32e-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference rootRef = database.getReference();
        DatabaseReference roomsRef = rootRef.child("rooms");


        roomsRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Map<String, Map<String, Object>> data = ( Map<String, Map<String, Object>>) task.getResult().getValue();
                Log.d("firebase", String.valueOf(data));

                if(data == null || (long) data.values().size() == 0){
                    CreateAndWait(roomsRef);
                    return;
                }


                Log.d("firebase-0", String.valueOf(data.entrySet().size()));
                for (Map.Entry<String, Map<String, Object>> room : data.entrySet()) {

                    Log.d("firebase-1", String.valueOf(room.getValue().get("Active")));
                    if(!(Boolean) room.getValue().get("Active")){
                        StartGame(roomsRef.child(room.getKey()), room.getValue());
                        return;
                    }
                }
                CreateAndWait(roomsRef);
            }
        });


    }

    public void StartGame(DatabaseReference roomRef, Map<String, Object> roomValue){
        roomValue.put("Active", true);
        roomValue.put("Board",  new ArrayList<Integer>(List.of(-1,-1,-1,-1,-1,-1,-1,-1,-1)));
        roomValue.put("Current",  0);
        roomRef.setValue(roomValue);
        Intent i = new Intent(OnlineLoadingus.this, OnlineGameActivity.class);
        i.putExtra("localPlayer", 0);
        i.putExtra("roomRef", roomRef.toString().substring(roomRef.getRoot().toString().length()));
        startActivity(i);
    }

    public void CreateAndWait(DatabaseReference roomsRef){
        roomRef = roomsRef.push();
        Map<String, Object> roomData = new HashMap<>();
        roomData.put("Active", false);
        roomRef.setValue(roomData);
        Log.d("firebase", "Waiting");

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Map<String, Object> room = (Map<String, Object>) dataSnapshot.getValue();
                Log.d("firebase", String.valueOf(room));

                if((Boolean) room.get("Active")){
                    Intent i = new Intent(OnlineLoadingus.this, OnlineGameActivity.class);
                    i.putExtra("localPlayer", 1);
                    i.putExtra("roomRef", roomRef.toString().substring(roomRef.getRoot().toString().length()));

                    startActivity(i);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        roomRef.addValueEventListener(postListener);
    };

    @Override
    public void onPause()
    {
        super.onPause();

        if(roomRef != null)
            roomRef.removeEventListener(postListener);

    }

}