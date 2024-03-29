package com.merp.tictactoe;

import android.content.Context;
import android.content.Intent;
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

    private int gameWinner = -1;//storing locally and then we'll use this to declare the winner or declare draw
    private boolean hasAWinner = false;
    private boolean playerOnesTurn = true;

    private static final String TAG = "premDebug";
    Context mContext;
    int playerId = -1;
    int thisPlayersTurn;
    int chances;
    int clickedBoxStatus;
    TextView statusText;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference boxStatusRef;
    private DatabaseReference topBoxStatusRef;
    private DatabaseReference whichPlayersTurnRef;
    private DatabaseReference gameOverRef;
    private DatabaseReference winnerRef;
    private DatabaseReference chancesLeftRef;
    private DatabaseReference trialDataBaseRef;

    ImageView i1;
    ImageView i2;
    ImageView i3;
    ImageView i4;
    ImageView i5;
    ImageView i6;
    ImageView i7;
    ImageView i8;
    ImageView i9;
    private DatabaseReference bStatus1;
    private DatabaseReference bStatus2;
    private DatabaseReference bStatus3;
    private DatabaseReference bStatus4;
    private DatabaseReference bStatus5;
    private DatabaseReference bStatus6;


    private DatabaseReference bStatus7;
    private DatabaseReference bStatus8;
    private DatabaseReference bStatus9;

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: pressed");
        super.onBackPressed();
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");
        setContentView(R.layout.online_game_layout);
        mAuth = FirebaseAuth.getInstance();
        mContext = this;
        statusText = findViewById(R.id.statusTextViewOnline);

        i1 = findViewById(R.id.imageView1Online);
        i2 = findViewById(R.id.imageView2Online);
        i3 = findViewById(R.id.imageView3Online);
        i4 = findViewById(R.id.imageView4Online);
        i5 = findViewById(R.id.imageView5Online);
        i6 = findViewById(R.id.imageView6Online);
        i7 = findViewById(R.id.imageView7Online);
        i8 = findViewById(R.id.imageView8Online);
        i9 = findViewById(R.id.imageView9Online);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: on start called");
        hasAWinner = false;//resetting that theres no winner
        thisPlayersTurn = 1;//resetting the playersTurn to player1
        Log.i(TAG, "onStart: RESETTING BOX STATUS LOCAL");

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
//            trialDataBaseRef = database.getReference("/BoxStatus");
//            trialDataBaseRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.i(TAG, "onDataChange: trying to directly print snapshot : " + snapshot.getValue().toString());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            //

            //checking if individual boxstatus is updated and if yes then update the ui

            //setting the db refrences
            topBoxStatusRef = database.getReference("/BoxStatus");
            bStatus1 = database.getReference("/BoxStatus/1");
            bStatus2 = database.getReference("/BoxStatus/2");
            bStatus3 = database.getReference("/BoxStatus/3");
            bStatus4 = database.getReference("/BoxStatus/4");
            bStatus5 = database.getReference("/BoxStatus/5");
            bStatus6 = database.getReference("/BoxStatus/6");
            bStatus7 = database.getReference("/BoxStatus/7");
            bStatus8 = database.getReference("/BoxStatus/8");
            bStatus9 = database.getReference("/BoxStatus/9");
            //


            //setting the value of boolean variable playerOnesTurn ( which helps in checking whos the winner )
            whichPlayersTurnRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int whichPlayersTurnCheck = Integer.parseInt(snapshot.getValue().toString());
                    if (whichPlayersTurnCheck == 1) {
                        playerOnesTurn = true;
                        Log.i(TAG, "onDataChange: playerOnesTurn set to TRUE");
                    } else {
                        playerOnesTurn = false;
                        Log.i(TAG, "onDataChange: playerOnesTurn set to FALSE");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //


            //these will look for the change and if theres any update the ui
            //also when we're calling checKWin function , i used playerOnesTurn to true always bc i hadnt initialsed it ,
            //so check how to solve that , only then will we know whos the winner , otherwise no
            bStatus1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: changed box boxStatusLocal1 to 1");
                        Log.i(TAG, "onDataChange: change image to  x");
                        i1.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: changed box boxStatusLocal1 to 2");
                        Log.i(TAG, "onDataChange: change image to O");
                        i1.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i1.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(1, playerOnesTurn)) {
                        Log.i(TAG, "onDataChange: checkWin called with playerOnesTurn=" + playerOnesTurn + " and local box stattus is ");
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: changed box boxStatusLocal2 to 1");
                        Log.i(TAG, "onDataChange: change image to  x");
                        i2.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: changed box boxStatusLocal2 to 2");
                        Log.i(TAG, "onDataChange: change image to O");
                        i2.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i2.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(2, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: change image to  x");
                        i3.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: change image to O");
                        i3.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i3.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(3, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: change image to  x");
                        i4.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: change image to O");
                        i4.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i4.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(4, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: change image to  x");
                        i5.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: change image to O");
                        i5.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i5.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(5, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus6.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: change image to  x");
                        i6.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: change image to O");
                        i6.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i6.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(6, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus7.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: change image to  x");
                        i7.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: change image to O");
                        i7.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i7.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(7, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus8.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: change image to  x");
                        i8.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: change image to O");
                        i8.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i8.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(8, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            bStatus9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = Integer.parseInt(snapshot.getValue().toString());
                    if (x == 1) {
                        Log.i(TAG, "onDataChange: change image to  x");
                        i9.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    } else if (x == 2) {
                        Log.i(TAG, "onDataChange: change image to O");
                        i9.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    } else if (x == -1) {
                        i9.setBackgroundResource(android.R.color.transparent);
                    }
                    if (checkWin(9, playerOnesTurn)) {
                        endGame();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //

            //


        }
    }

    public void clickedBoxOnline(View view) {


        final ImageView box = (ImageView) view;
        final int boxClicked = Integer.parseInt(box.getTag().toString());

        //setting the value of chancesLeft
        chancesLeftRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chances = Integer.parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //setting the value of thisPlayersTurn
        whichPlayersTurnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                thisPlayersTurn = Integer.parseInt(snapshot.getValue().toString());


                Log.i(TAG, "onCancelled: thisPlayersTurn set to " + thisPlayersTurn);

                if (playerId == thisPlayersTurn) {//to check if its the players turn


                    //setting clickedBoxStatus of the current box clicked
                    boxStatusRef = database.getReference("/BoxStatus/" + box.getTag().toString());
                    boxStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i(TAG, "box clicked " + boxClicked + " had status " + snapshot.getValue().toString());
                            clickedBoxStatus = Integer.parseInt(snapshot.getValue().toString());
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                } else {
                    Log.i(TAG, "clickedBoxOnline: not your turn");
                    Toast.makeText(mContext, "Wait for your turn", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                thisPlayersTurn = -2;
                Log.i(TAG, "onCancelled: thisPlayersTurn set to -2");
            }
        });


    }


    //below is copied from inGameActivity , might have errs
    boolean checkWin(int boxChanged, boolean playerOnesTurn) {

        int winningMaterial = -1; // winning material is just the value that should be found in boxStatus i.e either x or o
        int player; // 1:player one(x)    2:player two(o)
        if (playerOnesTurn) {
            winningMaterial = 1;
            player = 1;
        } else {
            winningMaterial = 2;
            player = 2;
        }

        //trying this : lets just create an offline copy right now
        Log.i(TAG, "checkWin: get a copy of firebase db into an array nw ");

        final int[] boxStatusLocal = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
        topBoxStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pushToArray(snapshot.getValue().toString(), boxStatusLocal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //check rows
        if (boxChanged == 1 || boxChanged == 2 || boxChanged == 3) {
            //check first row
            if (boxStatusLocal[0] == winningMaterial && boxStatusLocal[1] == winningMaterial && boxStatusLocal[2] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 4 || boxChanged == 5 || boxChanged == 6) {
            //check second row
            if (boxStatusLocal[3] == winningMaterial && boxStatusLocal[4] == winningMaterial && boxStatusLocal[5] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 7 || boxChanged == 8 || boxChanged == 9) {
            //check third row
            if (boxStatusLocal[6] == winningMaterial && boxStatusLocal[7] == winningMaterial && boxStatusLocal[8] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        }
        //check columns
        if (boxChanged == 1 || boxChanged == 4 || boxChanged == 7) {
            //check first column
            if (boxStatusLocal[0] == winningMaterial && boxStatusLocal[3] == winningMaterial && boxStatusLocal[6] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 2 || boxChanged == 5 || boxChanged == 8) {
            //check second column
            if (boxStatusLocal[1] == winningMaterial && boxStatusLocal[4] == winningMaterial && boxStatusLocal[7] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 3 || boxChanged == 6 || boxChanged == 9) {
            //check third column
            if (boxStatusLocal[2] == winningMaterial && boxStatusLocal[5] == winningMaterial && boxStatusLocal[8] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        }

        //if it lies on diagonal check the diagonal/(s)

        //main diagonal
        if (boxChanged == 1 || boxChanged == 5 || boxChanged == 9) {
            if (boxStatusLocal[0] == winningMaterial && boxStatusLocal[4] == winningMaterial && boxStatusLocal[8] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        }
        //second diagonal
        if (boxChanged == 7 || boxChanged == 5 || boxChanged == 3) {
            if (boxStatusLocal[6] == winningMaterial && boxStatusLocal[4] == winningMaterial && boxStatusLocal[2] == winningMaterial) {/*player won*/
                gameWinner = player;
                hasAWinner = true;
                return true;
            }
        }

        return false;
    }

    public void endGame() {
        if (hasAWinner) {
            Log.i(TAG, "endGame: Player " + gameWinner + " WINS!");
            Toast.makeText(this, "Game Over ! Player " + gameWinner + " WINS!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        } else {
            Log.i(TAG, "endGame: its a draw");
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }
    }


    public void pushToArray(String s, int[] array) {
        //convert this string to array format
        int arrayCtr = -1;
        for (int i = 5; i < s.length(); i++) {
            Log.i(TAG, "pushToArray: found " + s.charAt(i));
            StringBuilder stringBuilder;
            if (s.charAt(i) == ',') {
                arrayCtr++;
            }

        }
    }


}
