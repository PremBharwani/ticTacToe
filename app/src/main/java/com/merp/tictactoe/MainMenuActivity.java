package com.merp.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
    }

    public void clickPlayWithFriendOffline(View view) {
        startActivity(new Intent(this, InGameActivity.class));
    }

    public void menuPlayOnlineClicked(View view) {
        startActivity(new Intent(this, LoginScreenActivity.class));
    }
}
