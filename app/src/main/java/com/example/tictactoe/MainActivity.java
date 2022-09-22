package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean gameActive = true;
    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    public static int counter = 0;

    public static boolean isBot = false;

    ArrayList<Integer> available_spaces = new ArrayList<>();

    public void resetArray(){
        available_spaces.add(0);
        available_spaces.add(1);
        available_spaces.add(2);
        available_spaces.add(3);
        available_spaces.add(4);
        available_spaces.add(5);
        available_spaces.add(6);
        available_spaces.add(7);
        available_spaces.add(8);
    }

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (!gameActive) {
            gameReset();
        }

        if (gameState[tappedImage] == 2) {
            counter++;

            if (counter == 9) {
                gameActive = false;
            }

            gameState[tappedImage] = activePlayer;
            for(int i = 0; i < available_spaces.size();i++){
                if(available_spaces.get(i) == tappedImage){
                    available_spaces.remove(i);
                    break;
                }
            };
            Log.d("d", String.valueOf(available_spaces));
            img.setTranslationY(-1000f);

            if (activePlayer == 0) {
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                TextView status = findViewById(R.id.status);
                status.setTextColor(Color.GREEN);
                status.setText("Circle");
                int hasWon = 0;
                for (int[] winPosition : winPositions) {
                    if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                            gameState[winPosition[1]] == gameState[winPosition[2]] &&
                            gameState[winPosition[0]] != 2) {
                        hasWon = 1;

                        String winnerStr;

                        gameActive = false;
                        status.setText("");
                        if (gameState[winPosition[0]] == 0) {
                            winnerStr = "Crosses won";
                        } else {
                            winnerStr = "Circles won";
                        }
                        Intent intent = new Intent(this, ResultActivity.class);
                        intent.putExtra("result",winnerStr);
                        startActivity(intent);
                    }
                }
                if (counter == 9 && hasWon == 0) {
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("result","Draw");
                    startActivity(intent);
                }
                if(isBot && available_spaces.size()>0&&hasWon!=1){
                    status.setTextColor(Color.RED);
                    status.setText("Cross");
                    Random rand = new Random();
                    int n = rand.nextInt(available_spaces.size());
                    gameState[available_spaces.get(n)] = 1;
                    activePlayer = 0;
                    counter++;
                    ImageView imagus = findViewById(R.id.imageView0);
                    switch (available_spaces.get(n)){
                        case 0:
                            imagus = findViewById(R.id.imageView0);
                            break;
                        case 1:
                            imagus = findViewById(R.id.imageView1);
                            break;
                        case 2:
                            imagus = findViewById(R.id.imageView2);
                            break;
                        case 3:
                            imagus = findViewById(R.id.imageView3);
                            break;
                        case 4:
                            imagus = findViewById(R.id.imageView4);
                            break;
                        case 5:
                            imagus = findViewById(R.id.imageView5);
                            break;
                        case 6:
                            imagus = findViewById(R.id.imageView6);
                            break;
                        case 7:
                            imagus = findViewById(R.id.imageView7);
                            break;
                        case 8:
                            imagus = findViewById(R.id.imageView8);
                            break;
                    };
                    imagus.setImageResource(R.drawable.o);
                    available_spaces.remove(n);

                }
            } else {
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                TextView status = findViewById(R.id.status);
                status.setTextColor(Color.RED);
                status.setText("Cross");
            }
            img.animate().translationYBy(1000f).setDuration(0);
        }
        int hasWon = 0;
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {
                hasWon = 1;

                String winnerStr;

                gameActive = false;
                TextView status = findViewById(R.id.status);
                status.setText("");
                if (gameState[winPosition[0]] == 0) {
                    winnerStr = "Cross won";
                } else {
                    winnerStr = "Circles won";
                }
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("result",winnerStr);
                if(getIntent().hasExtra("mode")){
                    intent.putExtra("mode","BOT");
                }
                startActivity(intent);
            }
        }
        if (counter == 9 && hasWon == 0) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("result","Draw");
            startActivity(intent);
        }
    }

    public void gameReset() {
        counter = 0;
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }
        resetArray();
        ((ImageView) findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView8)).setImageResource(0);

        TextView status = findViewById(R.id.status);
        status.setTextColor(Color.RED);
        status.setText("Cross");
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBot = false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().hasExtra("mode")){
            isBot = true;
        }
        gameReset();
    }

}