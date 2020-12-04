package com.merp.tictactoe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InGameActivity extends AppCompatActivity {

    private static final String TAG = "premIsDebugging";
    Context context = this;

    ImageView b1, b2, b3, b4, b5, b6, b7, b8, b9;
    TextView statusText;

    int[] boxStatus = {-1, -1, -1, -1, -1, -1, -1, -1, -1};// (-1:not occupied) ; ( 0 : occupied by x) ; (1: occupied by O)
    int winner = -1;
    int turnsLeft = 9; //keeps track of number of turns left
    boolean hasAWinner = false;
    int clickedElement;
    boolean turnPlayerOne = true; //variable that keeps track of whose turn it is .

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//link
        Log.i(TAG, "onCreate: Oncreate called");
        b1 = findViewById(R.id.imageView1);
        b2 = findViewById(R.id.imageView2);
        b3 = findViewById(R.id.imageView3);
        b4 = findViewById(R.id.imageView4);
        b5 = findViewById(R.id.imageView5);
        b6 = findViewById(R.id.imageView6);
        b7 = findViewById(R.id.imageView7);
        b8 = findViewById(R.id.imageView8);
        b9 = findViewById(R.id.imageView9);
        statusText = findViewById(R.id.statusTextView);

    }    //Executes first

    public void clickedBox(View view) {

        ImageView box = (ImageView) view;
        String x = box.getTag().toString();
        clickedElement = Integer.parseInt(x); //getting value from 1-9 (take care while using in arrays)

        if (boxStatus[clickedElement - 1] == -1) {
            //fresh box
            turnsLeft--;
            if (turnPlayerOne) {
                //playerOne did the click
                Log.i(TAG, "clickedBox: playerOne clicked " + clickedElement);
                boxStatus[clickedElement - 1] = 0;
                box.setImageResource(R.drawable.tic_tac_toe_x); //change the image to x
                if (checkWin(clickedElement, turnPlayerOne)) {
                    endGame();
                } //this basically checks if the game has ended
                turnPlayerOne = false;
            } else {
                //playerTwo did the click
                Log.i(TAG, "clickedBox: playerTwo clicked " + clickedElement);
                boxStatus[clickedElement - 1] = 1;
                box.setImageResource(R.drawable.tic_tac_toe_o); //change image to 'o'
                if (checkWin(clickedElement, turnPlayerOne)) {
                    endGame();
                }
                turnPlayerOne = true;
            }
            if (turnsLeft <= 0) {
                endGame();
            }
        } else {
            //has been used already
            Toast.makeText(this, "Already filled", Toast.LENGTH_SHORT).show();
        }

    }      // And also checks for any clicks . Checks for the status of the boxes .

    boolean checkWin(int boxChanged, boolean playerOnesTurn) {

        int winningMaterial = -1; // winning material is just the value that should be found in boxStatus i.e either x or o
        int player = -1; // 0:player one(x)    1:player two(o)
        if (playerOnesTurn) {
            winningMaterial = 0;
            player = 0;
        } else {
            winningMaterial = 1;
            player = 1;
        }

        //check rows
        if (boxChanged == 1 || boxChanged == 2 || boxChanged == 3) {
            //check first row
            if (boxStatus[0] == winningMaterial && boxStatus[1] == winningMaterial && boxStatus[2] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 4 || boxChanged == 5 || boxChanged == 6) {
            //check second row
            if (boxStatus[3] == winningMaterial && boxStatus[4] == winningMaterial && boxStatus[5] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 7 || boxChanged == 8 || boxChanged == 9) {
            //check third row
            if (boxStatus[6] == winningMaterial && boxStatus[7] == winningMaterial && boxStatus[8] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        }
        //check columns
        if (boxChanged == 1 || boxChanged == 4 || boxChanged == 7) {
            //check first column
            if (boxStatus[0] == winningMaterial && boxStatus[3] == winningMaterial && boxStatus[6] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 2 || boxChanged == 5 || boxChanged == 8) {
            //check second column
            if (boxStatus[1] == winningMaterial && boxStatus[4] == winningMaterial && boxStatus[7] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        } else if (boxChanged == 3 || boxChanged == 6 || boxChanged == 9) {
            //check third column
            if (boxStatus[2] == winningMaterial && boxStatus[5] == winningMaterial && boxStatus[8] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        }

        //if it lies on diagonal check the diagonal/(s)

        //main diagonal
        if (boxChanged == 1 || boxChanged == 5 || boxChanged == 9) {
            if (boxStatus[0] == winningMaterial && boxStatus[4] == winningMaterial && boxStatus[8] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        }
        //second diagonal
        if (boxChanged == 7 || boxChanged == 5 || boxChanged == 3) {
            if (boxStatus[6] == winningMaterial && boxStatus[4] == winningMaterial && boxStatus[2] == winningMaterial) {/*player won*/
                winner = player + 1;
                hasAWinner = true;
                return true;
            }
        }

        return false;
    }   //checks the logic/whatever (Brains)

    void endGame() {
        statusText.setText("GAME OVER !");
        String displayTextEndgame;
        if (hasAWinner) {
            displayTextEndgame = "Player " + winner + " wins!";
        } else {
            displayTextEndgame = "Draw.";
        }

        new AlertDialog.Builder(context)
                .setTitle(displayTextEndgame)
                .setMessage("Do you want to play again?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //go to main menu
                        Log.i(TAG, "onClick: should go to menu");
                        startActivity(new Intent(context, MainMenuActivity.class));
                        finish();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        reset();
                        startActivity(new Intent(context, InGameActivity.class));
                        finish();
                    }
                })
                .show();
    }   //Ends game
}