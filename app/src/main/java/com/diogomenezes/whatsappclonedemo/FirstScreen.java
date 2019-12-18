package com.diogomenezes.whatsappclonedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.diogomenezes.whatsappclonedemo.parse_activities.ParseLoginActivity;
import com.diogomenezes.whatsappclonedemo.ui.contactList.MainAct;
import com.parse.ParseUser;

public class FirstScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        getSupportActionBar().hide();

        //GET SESSION TOKEN
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, MainAct.class));
            finish();
        }
    }

    public void agreeClicked(View view) {
        startActivity(new Intent(this, ParseLoginActivity.class));
//        finish();
    }
}
