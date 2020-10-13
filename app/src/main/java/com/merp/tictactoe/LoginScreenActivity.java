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

public class LoginScreenActivity extends AppCompatActivity {

    private static final String TAG = "premDebug";
    Context mContext;
    EditText email, pass;
    private FirebaseAuth mAuth;

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


    }

}
