package com.diogomenezes.whatsappclonedemo.parse_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diogomenezes.whatsappclonedemo.ui.contactList.MainAct;
import com.diogomenezes.whatsappclonedemo.R;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ParseLoginActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    private TextView userNameEdit, passEdit;
    private Button signInBtn, signUpBtn;

    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("WhatsApp Login");
        userNameEdit = findViewById(R.id.nameEditText);
        passEdit = findViewById(R.id.passEditText);
        signInBtn = findViewById(R.id.signInButton);
        signUpBtn = findViewById(R.id.signUpButton);

        if (ParseUser.getCurrentUser() != null) {
            redirect();
        }
    }

    private boolean checkFields() {
        if (!userNameEdit.getText().toString().isEmpty() && !passEdit.getText().toString().isEmpty()) {
            username = userNameEdit.getText().toString().trim();
            password = passEdit.getText().toString().trim();
            return true;
        } else {
            Toast.makeText(this, "Name/Password Required", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void redirect() {
        startActivity(new Intent(this, MainAct.class));
        finish();

    }

    public void signUpClick(View view) {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    System.out.println("nice");
                    redirect();
                } else {
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        });
    }


    public void signInClick(View view) {
        if (checkFields()) {
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                        Toast.makeText(ParseLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        redirect();
                    }
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
