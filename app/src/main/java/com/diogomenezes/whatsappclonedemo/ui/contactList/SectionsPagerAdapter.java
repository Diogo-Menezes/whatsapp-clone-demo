package com.diogomenezes.whatsappclonedemo.ui.contactList;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.ui.contactList.fragments.CallHistoryFragment;
import com.diogomenezes.whatsappclonedemo.ui.contactList.fragments.CameraFragment;
import com.diogomenezes.whatsappclonedemo.ui.contactList.fragments.ContactListFragment;
import com.diogomenezes.whatsappclonedemo.ui.contactList.fragments.StoriesFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_0, R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = CameraFragment.newInstance(0);

                break;
            case 1:
                fragment = new ContactListFragment();
                break;
            case 2:
                fragment = new StoriesFragment();
                break;
            case 3:
                fragment = new CallHistoryFragment();
                break;
            default:
                break;
        }
        if (fragment.equals(ContactListFragment.class)) {
            Toast.makeText(mContext, "Camera", Toast.LENGTH_SHORT).show();
        }
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {

        }
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 4;
    }
}