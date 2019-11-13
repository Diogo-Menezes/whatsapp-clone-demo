package com.diogomenezes.whatsappclonedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.diogomenezes.whatsappclonedemo.parse_Activities.ParseLoginActivity;
import com.diogomenezes.whatsappclonedemo.ui.contactList.Main;

public class FirstScreen extends AppCompatActivity {
    private boolean isLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        getSupportActionBar().hide();

        //GET SESSION TOKEN
        if (isLogged) {
            finish();
            startActivity(new Intent(this, Main.class));
        }
    }

    public void agreeClicked(View view) {
        startActivity(new Intent(this, ParseLoginActivity.class));
    }
}
