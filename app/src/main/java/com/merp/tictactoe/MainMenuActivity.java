package com.merp.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);//links the layout with the  backend
    }

    public void clickPlayWithFriendOffline(View view) {

        startActivity(new Intent(this, InGameActivity.class));//screen(activity) change to the new activity

    }

    public void menuPlayOnlineClicked(View view) {
        startActivity(new Intent(this, LoginScreenActivity.class));
    }

    public void menuPlayOnlineClickedV2(View view) {
        startActivity(new Intent(this, OnlineGameActivityV2.class));
    }
}
