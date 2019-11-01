package com.diogomenezes.whatsappclonedemo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("3JHNpFENqjqomY9SRZ31k7pAwQO7rd1XOakWxiFL")
                .clientKey("FiOWG1ZBPSHjdtiGNDUyO9uif5qHtoA9bOe5F8df")
                .server("https://parseapi.back4app.com/")
                .build()
        );

//        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
