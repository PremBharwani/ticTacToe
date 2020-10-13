package com.merp.tictactoe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineGameActivity extends AppCompatActivity {

    private static final String TAG = "premDebug";
    Context mContext;
    int playerId = -1;
    int thisPlayersTurn;
    int chances;
    int clickedBoxStatus;
    boolean gotThisPlayersTurn = false;
    TextView statusText;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference boxStatusRef;
    private DatabaseReference whichPlayersTurnRef;
    private DatabaseReference gameOverRef;
    private DatabaseReference winnerRef;
    private DatabaseReference chancesLeftRef;
    private DatabaseReference trialDataBaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_game_layout);
        mAuth = FirebaseAuth.getInstance();
        mContext = this;
        statusText = findViewById(R.id.statusTextViewOnline);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Failed to authorise ", Toast.LENGTH_SHORT).show();
        } else {

            //assigning playerId to the current user (either 1 or 2)
            if (user.getEmail().equals("player1@test.com")) {
                playerId = 1;
//                Log.i(TAG, "onStart: playerId set to 1");
            } else {
                playerId = 2;
//                Log.i(TAG, "onStart: playerId set to 2");
            }
            Log.i(TAG, "onStart: inside the OnineGameActivity and user with playerId : " + playerId + " is logged into firebase");
            database = FirebaseDatabase.getInstance();
            gameOverRef = database.getReference("/gameOver");
            winnerRef = database.getReference("/winner");
            chancesLeftRef = database.getReference("/chancesLeft");
            whichPlayersTurnRef = database.getReference("/whichPlayersTurn");

            //trying to get changes in boxStatus branch directly
            trialDataBaseRef = database.getReference("/BoxStatus");
            trialDataBaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i(TAG, "onDataChange: trying to directly print snapshot : " + snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //

        }
    }

    public void clickedBoxOnline(View view) {

        gotThisPlayersTurn = false;

        //setting the value of thisPlayersTurn
        whichPlayersTurnRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                thisPlayersTurn = Integer.parseInt(snapshot.getValue().toString());
                gotThisPlayersTurn = true;
                Log.i(TAG, "onCancelled: thisPlayersTurn set to " + thisPlayersTurn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                thisPlayersTurn = -2;
                Log.i(TAG, "onCancelled: thisPlayersTurn set to -2");
            }
        });


        //setting the value of chancesLeft
        chancesLeftRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chances = Integer.parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (playerId == thisPlayersTurn) {//to check if its the players turn


            //setting clickedBoxStatus of the current box clicked
            ImageView box = (ImageView) view;
            final int boxClicked = Integer.parseInt(box.getTag().toString());
            boxStatusRef = database.getReference("/BoxStatus/" + box.getTag().toString());
            boxStatusRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i(TAG, "box clicked " + boxClicked + " had status " + snapshot.getValue().toString());
                    clickedBoxStatus = Integer.parseInt(snapshot.getValue().toString());
                    Log.i(TAG, "onDataChange: clickedBoxStatus set to " + clickedBoxStatus);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //check if the box clicked is empty or not.
            if (clickedBoxStatus == -1) {
                Log.i(TAG, "the clicked box is not occupied");


                //update the firebase boxStatus
                boxStatusRef.setValue(playerId);

                //now we're changing the whichPlayersTurn and also reducing the chances left in firebase
                if (playerId == 1) {
                    //change this playersturn to 2
                    whichPlayersTurnRef.setValue(2);
                    chances--;
                    chancesLeftRef.setValue(chances);
                } else if (playerId == 2) {
                    //change this playersturn to 1
                    whichPlayersTurnRef.setValue(1);
                    chances--;
                    chancesLeftRef.setValue(chances);
                }

            } else {
                Toast.makeText(mContext, "this box is filled already ", Toast.LENGTH_SHORT).show();
            }


        } else {
            Log.i(TAG, "clickedBoxOnline: not your turn");
            Toast.makeText(mContext, "Wait for your turn", Toast.LENGTH_SHORT).show();
        }


    }


}
