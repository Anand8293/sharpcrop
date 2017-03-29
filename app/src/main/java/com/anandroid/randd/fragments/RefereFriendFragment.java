package com.anandroid.randd.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anandroid.randd.sharpcrop.R;

public class RefereFriendFragment extends Fragment {

    public RefereFriendFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RefereFriendFragment newInstance() {
        RefereFriendFragment fragment = new RefereFriendFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_refere_friend, container, false);
         view.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String shareBody = "Refer And Earn Rs. 50! \n Gift A Friend Rs 50 And Earn 50 on First Services \n sds4567";
                 Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                 sharingIntent.setType("text/plain");
                 sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SharpCrop Reference");
                 sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                 startActivity(sharingIntent);
             }
         });
        return view;
    }

}
