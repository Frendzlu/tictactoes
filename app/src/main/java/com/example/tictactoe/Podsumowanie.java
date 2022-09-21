package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Podsumowanie extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podsumowanie);
        Button again = (Button) findViewById(R.id.play_again);
        Button main = (Button) findViewById(R.id.main_menu);
        Intent againI = new Intent(this, MainActivity.class);
        Intent menuI = new Intent(this, MainMenu.class);
        TextView results = (TextView) findViewById(R.id.Results);
        Bundle extras = getIntent().getExtras();
        results.setText(extras.getString("result"));
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("mode")){
                    againI.putExtra("mode","BOT");
                }
                startActivity(againI);
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(menuI);
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
}