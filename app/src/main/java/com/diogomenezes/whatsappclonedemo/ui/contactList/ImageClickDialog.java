package com.diogomenezes.whatsappclonedemo.ui.contactList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.ui.chat.ChatActivity;

import static com.diogomenezes.whatsappclonedemo.ui.contactList.MainActivity.FRIEND_ID;
import static com.diogomenezes.whatsappclonedemo.ui.contactList.MainActivity.FRIEND_IMAGE;
import static com.diogomenezes.whatsappclonedemo.ui.contactList.MainActivity.FRIEND_NAME;


/*
 *
 *  Dialog that results from the clicking in the contact image
 *  in the FriendListActivity
 *
 * */

public class ImageClickDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "ContactInfoDialog";

    private TextView contactName;
    private ImageView contactImage, videoCallAction, phoneCallAction, messageAction, detailsAction;
    private String mName;
    private String mImage;
    private int mId;
    private int position;
    private AlertDialog.Builder builder;

    public ImageClickDialog() {
    }

    public ImageClickDialog(String name, String image, int id, int position) {
        this.mName = name;
        this.mImage = image;
        this.mId = id;
        this.position = position;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_info_layout, container, false);
        contactName = view.findViewById(R.id.contactNameInfo);
        contactImage = view.findViewById(R.id.contactImageInfo);
        videoCallAction = view.findViewById(R.id.video_call_contact_dialog_action);
        phoneCallAction = view.findViewById(R.id.phone_call_contact_dialog_action);
        messageAction = view.findViewById(R.id.message_contact_dialog_action);
        detailsAction = view.findViewById(R.id.contact_details_dialog_action);

        videoCallAction.setOnClickListener(this);
        phoneCallAction.setOnClickListener(this);
        messageAction.setOnClickListener(this);
        detailsAction.setOnClickListener(this);


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


    public void videoClick(View view) {
        Toast.makeText(view.getContext(), "VideoCall", Toast.LENGTH_SHORT).show();
//        dialogFragment.dismiss();
    }

    public void phoneClick(View view) {
        Toast.makeText(view.getContext(), "Call", Toast.LENGTH_SHORT).show();
//        dialogFragment.dismiss();
    }

    public void messageClick(View view) {
        Log.d(TAG, "messageClick: Clicked ");
//        dialogFragment.dismiss();
    }

    public void contactInfoClick(View view) {
        Toast.makeText(view.getContext(), "Contact Info", Toast.LENGTH_SHORT).show();
//        dialogFragment.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_call_contact_dialog_action:
                Toast.makeText(getContext(), "video", Toast.LENGTH_SHORT).show();
                break;
            case R.id.phone_call_contact_dialog_action:
                Toast.makeText(getContext(), "phnone", Toast.LENGTH_SHORT).show();
                break;
            case R.id.message_contact_dialog_action:
                goChat();
                Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_details_dialog_action:
                Toast.makeText(getContext(), "details", Toast.LENGTH_SHORT).show();
                break;
        }
        dismiss();

    }

    private void goChat() {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(FRIEND_NAME, mName);
        intent.putExtra(FRIEND_ID, mId);
        intent.putExtra(FRIEND_IMAGE, mImage);
        startActivity(intent);
    }
}
