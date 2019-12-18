package com.diogomenezes.whatsappclonedemo.ui.contactList;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.diogomenezes.whatsappclonedemo.FirstScreen;
import com.diogomenezes.whatsappclonedemo.R;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

public class MainAct extends AppCompatActivity {
    private static final String TAG = "FriendListActivity";
    public static final String FRIEND_ID = "friend_id";
    public static final String FRIEND_IMAGE = "friend_image_uri";
    public static final String FRIEND_NAME = "friend_name";
    public static int USER_ID = 100;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Toolbar toolbar = findViewById(R.id.toolbar_contact_list);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 1, this);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_camera_white);
        viewPager.setCurrentItem(1);
        tabs.getTabAt(0).setIcon(drawable);
        tabs.setTabIconTint(ColorStateList.valueOf(Color.WHITE));


        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.getText().removeObservers(this);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged: " + s);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: called"+" "+ item.getItemId() +" "+ R.id.action_logout);
        if (item.getItemId() == R.id.action_logout) {
            Log.i(TAG, "onOptionsItemSelected: logout called");
            ParseUser.logOut();
            startActivity(new Intent(this, FirstScreen.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(1);
        }
    }
}