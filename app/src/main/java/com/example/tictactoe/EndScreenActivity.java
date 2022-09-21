package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndScreenActivity extends AppCompatActivity {
    boolean onnlie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);
        Bundle extras = getIntent().getExtras();
        TextView tv = findViewById(R.id.result);
        if (extras != null) {
            int won = extras.getInt("won");
            int local = extras.getInt("local", -1);
            if(local == -1){
                if(won == -1){
                    tv.setText("DRAW");
                }
                if(won == 0){
                    tv.setText("Player 1 WON (X)");
                }
                if(won == 1){
                    tv.setText("Player 2 WON (O)");
                }
            }
            else {
                onnlie = true;

                if(won == -1){
                    tv.setText("DRAW");
                }
                else if(won != local){
                    tv.setText("You won");
                }
                else{
                    tv.setText("You lost");
                }
            }
        }
    }

    public void Play(View view) {
        if(onnlie){
            Intent intent = new Intent(this, OnlineLoadingus.class);
            startActivity(intent);

        }
        else {

            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
    }

    public void Return(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}