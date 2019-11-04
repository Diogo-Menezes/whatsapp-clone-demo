package com.diogomenezes.whatsappclonedemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;


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
    private int mImage;
    private AlertDialog.Builder builder;

    public ImageClickDialog() {
    }

    public ImageClickDialog(String name, int image) {
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
            mImage = savedInstanceState.getInt("image");
        }

        contactName.setText(mName);
        contactImage.setImageDrawable(ActivityCompat.getDrawable(view.getContext(), mImage));
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
        if (getDialog() !=null){
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
        outState.putInt("image", mImage);
    }
}
