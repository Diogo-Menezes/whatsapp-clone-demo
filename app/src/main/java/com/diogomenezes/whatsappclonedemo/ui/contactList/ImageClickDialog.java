package com.diogomenezes.whatsappclonedemo.ui.contactList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.diogomenezes.whatsappclonedemo.R;


/*
 *
 *  Dialog that results from the clicking in the contact image
 *  in the FriendListActivity
 *
 * */

public class ImageClickDialog extends DialogFragment {
    private static final String TAG = "ContactInfoDialog";

    private TextView contactName;
    private ImageView contactImage;
    private String mName;
    private String mImage;
    private AlertDialog.Builder builder;

    public ImageClickDialog() {
    }

    public ImageClickDialog(String name, String image) {
        this.mName = name;
        this.mImage = image;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_info_layout, container, false);
        contactName = view.findViewById(R.id.contactNameInfo);
        contactImage = view.findViewById(R.id.contactImageInfo);

        if (savedInstanceState != null) {
            mName = savedInstanceState.getString("name");
            mImage = savedInstanceState.getString("image");
        }

        contactName.setText(mName);
        Glide.with(getContext()).load(mImage).into(contactImage);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        Log.i(TAG, "onCreateDialog: Called");


        return builder.show();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: called");
        if (getDialog() != null) {
            int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
            int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
            getDialog().getWindow().setLayout(width, height);
        }
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", mName);
        outState.putString("image", mImage);
    }
}
