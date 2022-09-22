package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private int[][] board = {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
    private int current = 0; //0 - player 0 (X)  1 - player 1 (O)
    private int moves = 0;
    private TextView p0;
    private TextView p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        p0 = findViewById(R.id.TVP1);
        p1 = findViewById(R.id.TVP2);
    }

    public void onClick(View v){
        int x = -1;
        int y = -1;

        switch (v.getId()) {
            case R.id.bt1:
                x = 0;
                y = 0;
                break;
            case R.id.bt2:
                x = 1;
                y = 0;
                break;
            case R.id.bt3:
                x = 2;
                y = 0;
                break;
            case R.id.bt4:
                x = 0;
                y = 1;
                break;
            case R.id.bt5:
                x = 1;
                y = 1;
                break;
            case R.id.bt6:
                x = 2;
                y = 1;
                break;
            case R.id.bt7:
                x = 0;
                y = 2;
                break;
            case R.id.bt8:
                x = 1;
                y = 2;
                break;
            case R.id.bt9:
                x = 2;
                y = 2;
                break;
        }
        if(board[x][y] != -1)
            return;

        change(v);

        board[x][y] = this.current;
        int a = ended();
        moves++;
        Log.i("XXX", String.valueOf(a));
        if(a != -1 || moves == 9){
            Intent intent = new Intent(this, EndScreenActivity.class);
            intent.putExtra("won", a);
            intent.putExtra("moves", moves);
            startActivity(intent);
        }

        current = current == 0 ? 1 : 0;
        if(current == 0){
            p0.setBackgroundColor(getResources().getColor(R.color.purple_200));
            p1.setBackgroundColor(getResources().getColor(R.color.purple_500));
        }
        else {
            p0.setBackgroundColor(getResources().getColor(R.color.purple_500));
            p1.setBackgroundColor(getResources().getColor(R.color.purple_200));
        }

    }

    public void change(View v){
        ((TextView)v).setText(current == 0 ? "X" : "O");
    }

    public int ended(){
        int[][][] winPositions = {{{0, 0}, {0, 1}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 2}, {1, 2}, {2, 2}},
                {{0, 0}, {0, 1}, {0, 2}}, {{1, 0}, {1, 1}, {1, 2}}, {{2, 0}, {2, 1}, {2, 2}},
                {{0, 0}, {1, 1}, {2, 2}}, {{2, 0}, {1, 1}, {0, 2}}};

        for (int[][] winPosition : winPositions) {
            if (board[winPosition[0][0]][winPosition[0][1]] == board[winPosition[1][0]][winPosition[1][1]] &&
                    board[winPosition[1][0]][winPosition[1][1]] == board[winPosition[2][0]][winPosition[2][1]]) {
                return board[winPosition[0][0]][winPosition[0][1]];
            }
        }
        return -1;
//        outer:
//        for (int i = 0; i < 3; i++){
//            int a = board[i][0];
//            for (int j = 0; j < 3; j++){
//                if(a != board[i][j])
//                    continue outer;
//            }
//            return a;
//        }
//        outer:
//        for (int i = 0; i < 3; i++){
//            int a = board[0][i];
//            for (int j = 0; j < 3; j++){
//                if(a != board[j][i])
//                    continue outer;
//            }
//            return a;
//        }
//        int a = board[0][0];
//        boolean won = true;
//        for (int i = 0; i < 3; i++){
//            if(a != board[i][i])
//                won = false;
//        }
//        if(won)
//            return a;
//        a = board[0][2];
//        won = true;
//        for (int i = 0; i < 3; i++){
//            if(a != board[i][2 - i])
//                won = false;
//        }
//        if(won)
//            return a;
//
//        return -1;
    }


}