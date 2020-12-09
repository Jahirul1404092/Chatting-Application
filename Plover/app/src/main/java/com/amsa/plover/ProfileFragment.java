package com.amsa.plover;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private TextView profileNameTextView,profileMobileTextView,profileGenderTextView,profileIdTextView;
    private Button button;
    private ImageView profilePictureimageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        button = (Button) view.findViewById(R.id.button);
        profileNameTextView=(TextView)view.findViewById(R.id.profileNameTextViewId);
        profileMobileTextView=(TextView)view.findViewById(R.id.profileMobileTextViewId);
        profileGenderTextView=(TextView)view.findViewById(R.id.profileGenderTextViewId);
        profileIdTextView=(TextView)view.findViewById(R.id.profileIdTextViewId);
        profilePictureimageView=(ImageView)view.findViewById(R.id.profilePictureimageViewId);



        Home activity = (Home) getActivity();
        final String name = activity.getName();
        final String profileId = activity.getProfileId();
        final String mobile = activity.getMobile();
        final String gender= activity.getGender();
        final String imageName= activity.getImageName();
        final String imageDownloadUrl= activity.getImageDownloadUrl();
        System.out.println("mmmmmmm  "+name+" "+profileId+" "+mobile+" "+gender+" "+imageName+" "+ imageDownloadUrl);
        Picasso.with(view.getContext()).load(imageDownloadUrl).placeholder(R.drawable.male).fit().centerCrop().into(profilePictureimageView);
        profileNameTextView.setText("Name:  "+name);
        profileMobileTextView.setText("Phone:  "+mobile);
        profileGenderTextView.setText("Gender:  "+gender);
        profileIdTextView.setText("Id:"+profileId);


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something

            }
        });




        return view;




    }


}
