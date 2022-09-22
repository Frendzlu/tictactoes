package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnlineGameActivity extends AppCompatActivity {

    private int[][] board = {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
    private Long current = 0L; //0 - player 0 (X)  1 - player 1 (O)
    private int localPlayer;
    private int moves = 0;
    private TextView tvp;
    private DatabaseReference roomRef;
    private Map<String, Object> roomVal;
    ValueEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);
        tvp = findViewById(R.id.OTVP);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            localPlayer = extras.getInt("localPlayer");
            String roomUrl = extras.getString("roomRef");
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://tictactoes-2cbfe-default-rtdb.europe-west1.firebasedatabase.app/");
            roomRef = database.getReference(roomUrl);

            roomRef.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    roomVal = (Map<String, Object>) task.getResult().getValue();

                    Log.d("firebase", String.valueOf(roomVal));
                }
            });
            textV();

            //The key argument here must match that used in the other activity
        }

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Map<String, Object> room = (Map<String, Object>) dataSnapshot.getValue();
                Log.d("firebase", String.valueOf(room));
                roomVal = room;
                current = (Long) roomVal.get("Current");
                if(current == null)
                    current = -1L;
                if(roomVal.get("X")!= null){
                    Long x = (Long) roomVal.get("X");
                    Long y = (Long) roomVal.get("Y");
                    @SuppressLint("DefaultLocale") String id = "bt" + String.format("%01d", y*3 + x + 1);
                    int rId =  getId(id, R.id.class);
                    View v = findViewById(rId);
                    change(v, x.intValue(), y.intValue(), false);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        roomRef.addValueEventListener(postListener);

    }

    public void onClick(View v){
        Log.i("XXXGame", String.valueOf(localPlayer == current));
        Log.i("XXXGame", String.valueOf(current));
        if(this.localPlayer != this.current)
            return;
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


        change(v, x, y, true);

    }

    public void change(View v, int x, int y, boolean save){
        if(board[y][x] != -1){
            return;
        }

        if(save)
            current = current == 0L ? 1L : 0L;

        //((TextView)v).setText(current == 0 ? "X" : "O");
        if(current == 0){
            ((ImageView)v).setImageResource(R.drawable.x);
        }
        else{
            ((ImageView)v).setImageResource(R.drawable.o);
        }


        moves++;
        board[y][x] = Math.toIntExact(this.current);
        if(save){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.roomVal.put("Board", Arrays.stream(board).flatMapToInt(Arrays::stream).boxed().collect(Collectors.toList()));
            }
            roomVal.put("X" , x);
            roomVal.put("Y" , y);
            roomVal.put("Current" , current);
            roomRef.setValue(roomVal);


        }
        textV();
        int a = ended();
        Log.i("XXX", String.valueOf(a));
        if(a != -1 || moves == 9){
            Intent intent = new Intent(this, EndScreenActivity.class);
            intent.putExtra("won", a);
            intent.putExtra("local", localPlayer);
            intent.putExtra("moves", moves);
            startActivity(intent);
        }
    }

    public void textV(){
        if(current == localPlayer){
            tvp.setText("Your turn");
            tvp.setTextColor(getResources().getColor(R.color.purple_200));
        }
        else {
            tvp.setText("Enemy's turn");
            tvp.setTextColor(getResources().getColor(R.color.teal_200));
        }
    }

    public int ended(){
        int[][][] winPositions = {{{0, 0}, {1, 0}, {2, 0}}, {{0, 1}, {1, 1}, {2, 1}}, {{0, 2}, {1, 2}, {2, 2}},
                {{0, 0}, {0, 1}, {0, 2}}, {{1, 0}, {1, 1}, {1, 2}}, {{2, 0}, {2, 1}, {2, 2}},
                {{0, 0}, {1, 1}, {2, 2}}, {{2, 0}, {1, 1}, {0, 2}}};

        Log.d("SUSMOGUS", "=== NEW ===");
        for (int[][] winPosition : winPositions) {
            Log.d("SUSMOGUS", "=====");
            Log.d("SUSMOGUS", String.valueOf(board[winPosition[0][0]][winPosition[0][1]]));
            Log.d("SUSMOGUS", String.valueOf(board[winPosition[1][0]][winPosition[1][1]]));
            Log.d("SUSMOGUS", String.valueOf(board[winPosition[2][0]][winPosition[2][1]]));
            if (board[winPosition[0][0]][winPosition[0][1]] == board[winPosition[1][0]][winPosition[1][1]] &&
                    board[winPosition[1][0]][winPosition[1][1]] == board[winPosition[2][0]][winPosition[2][1]] &&
                    board[winPosition[0][0]][winPosition[0][1]] != -1) {
                return board[winPosition[0][0]][winPosition[0][1]];
            }
        }
        return -1;
    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if(roomRef != null)
            roomRef.removeEventListener(postListener);

    }

}