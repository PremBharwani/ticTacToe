package com.merp.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginScreenActivity extends AppCompatActivity {

    private static final String TAG = "premDebug";
    Context mContext;
    EditText email, pass;
    private FirebaseAuth mAuth;

    private DatabaseReference b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private DatabaseReference playerTurnRef;
    private DatabaseReference chanceLeft;
    private DatabaseReference winr;
    private DatabaseReference gOver;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);
        mAuth = FirebaseAuth.getInstance();
        mContext = this;

        email = findViewById(R.id.editTextTextEmailAddressLogin);
        pass = findViewById(R.id.editTextTextPasswordLogin);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) { //if the user is already signed in , allowing him to directly go to the game screen
            Log.i(TAG, "in login activity : FirebaseUser object is NOT null , so we're skipping login");
            startActivity(new Intent(mContext, OnlineGameActivity.class));

            db = FirebaseDatabase.getInstance();
            b1 = db.getReference("/BoxStatus/1");
            b2 = db.getReference("/BoxStatus/2");
            b3 = db.getReference("/BoxStatus/3");
            b4 = db.getReference("/BoxStatus/4");
            b5 = db.getReference("/BoxStatus/5");
            b6 = db.getReference("/BoxStatus/6");
            b7 = db.getReference("/BoxStatus/7");
            b8 = db.getReference("/BoxStatus/8");
            b9 = db.getReference("/BoxStatus/9");
            playerTurnRef = db.getReference("/whichPlayersTurn");
            chanceLeft = db.getReference("/chancesLeft");
            gOver = db.getReference("/gameOver");
            winr = db.getReference("/winner");

            resetFirebaseData();
        }
    }


    public void onlineLoginButtonClicked(View view) {
        Log.i(TAG, "onlineLoginButtonClicked: inside");

        //Signing in the user through firebase
        mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: sucessfully logged in");
                            resetFirebaseData();
                            startActivity(new Intent(mContext, OnlineGameActivity.class));
                        } else {
                            Log.i(TAG, "onComplete: Failed to log in : " + task.getException());
                            Toast.makeText(mContext, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void resetFirebaseData() {

        //if not already resetted , reset all the values and mark that it is reseted on the db
        Log.i(TAG, "resetFirebaseData: inside the resetDatabase method , well reset the database");

        b1.setValue(-1);
        b2.setValue(-1);
        b3.setValue(-1);
        b4.setValue(-1);
        b5.setValue(-1);
        b6.setValue(-1);
        b7.setValue(-1);
        b8.setValue(-1);
        b9.setValue(-1);
        playerTurnRef.setValue(1);
        chanceLeft.setValue(9);
        gOver.setValue(false);
        winr.setValue(-1);
    }

}
