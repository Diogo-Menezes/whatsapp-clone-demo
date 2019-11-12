package com.diogomenezes.whatsappclonedemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.diogomenezes.whatsappclonedemo.ui.contactList.fragments.ContactListFragment;
import com.diogomenezes.whatsappclonedemo.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
//
//        Fragment fragment = new ContactListFragment();
//        sectionsPagerAdapter.setPrimaryItem(viewPager, 1, fragment);
    }
}