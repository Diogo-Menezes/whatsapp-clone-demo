package com.diogomenezes.whatsappclonedemo.parse_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.ui.contactList.MainAct;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
        Intent intent = new Intent(this, MainAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public void signUpClick(View view) {
        ParseUser parseUser = new ParseUser();
        if (checkFields()) {
            parseUser.setUsername(username);
            parseUser.setPassword(password);

            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    String message = "";
                    if (e == null) {
                        message = "Sign up successfully";
                        redirect();
                    } else {
                        message = "Something went wrong :(";
                    }
                    Toast.makeText(ParseLoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void signInAnonymous(View view) {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    System.out.println("Sign up successfully");
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
        super.onBackPressed();
    }
}
