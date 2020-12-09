package com.amsa.plover;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MessageFragment extends Fragment {
    View view;
    private RecyclerView myrecyclerview;
    private List<Message> messageList;
    DatabaseReference databaseReference;
    public MessageFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_message, container, false);
        myrecyclerview=(RecyclerView) view.findViewById(R.id.message_recyclerviewId);
        MessageAdapter messageAdapter=new MessageAdapter(getContext(),messageList);
//        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity());

        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(false);           ////uporer line er bodole ei 4 ti line lekha hoiche bottom theke write korar jonno
        layoutManager.canScrollVertically();
        myrecyclerview.setLayoutManager(layoutManager);
        myrecyclerview.setAdapter(messageAdapter);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String profileId=new Home().getProfileId();
        final String name=new Home().getName();
        final String mobile=new Home().getMobile();
        final String imageName=new Home().getImageName();
        final String gender=new Home().getGender();
        final String imageDownloadUrl=new Home().getImageDownloadUrl();

        final String fprofileId=new Messaging().getFid();
        final String fname=new Messaging().getFname();
        final String fmobile=new Messaging().getFmobile();
        final String fpassword=new Messaging().getFpassword();
        final String fgender=new Messaging().getFgender();
        final String fimageDownloadUrl=new Messaging().getFimageUri().toString();
        String fnfId=profileId+"+";
        fnfId+=fprofileId;
        databaseReference= FirebaseDatabase.getInstance().getReference("Friendship").child(fnfId);
        messageList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Message message=dataSnapshot1.getValue(Message.class);

                    messageList.add(message);
                }
//                listView.setAdapter(customAdapter);
//                myrecyclerview.setAdapter(new ContactAdapter(getContext(),profileList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        super.onCreate();

    }
//        profileList.add(new Contact("A","65783",R.drawable.ic_group));
//        profileList.add(new Contact("B","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("C","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("D","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("E","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("F","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("G","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("H","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("I","36733452",R.drawable.ic_group));
//        profileList.add(new Contact("J","36733452",R.drawable.ic_group));
//    }
}


//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class ContactFragment extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_contact, container, false);
//    }
//
//
//}




//package com.example.chatapp;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Adapter;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChatFragment extends Fragment {
//
//    private ListView listView;
//
//    DatabaseReference databaseReference;
//    private List<Profile> profileList;
//    private ArrayAdapter customAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_chat, container, false);
//        return view;
//
//        listView=(ListView)view.findViewById(R.id.chatListViewId);
//        databaseReference= FirebaseDatabase.getInstance().getReference("Profile");
//
//        profileList=new ArrayList<>();
//        customAdapter=new ArrayAdapter(view.getContext(),R.layout.fragment_chat,profileList);
//        return view;
//    }
//    @Override
//    public void onStart() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                profileList.clear();
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    Profile profile=dataSnapshot1.getValue(Profile.class);
//                    profileList.add(profile);
//                }
//                listView.setAdapter(customAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        super.onStart();
//    }
//
//
//}




/*package com.example.chatapp;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    View view;
    private RecyclerView myrecyclerview;
    private List<Contact> contactList;
    public ContactFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_contact, container, false);
        myrecyclerview=(RecyclerView) view.findViewById(R.id.contact_recyclerviewId);
        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(getContext(),contactList);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactList=new ArrayList<>();
        contactList.add(new Contact("A","65783",R.drawable.ic_group));
        contactList.add(new Contact("B","36733452",R.drawable.ic_group));
        contactList.add(new Contact("C","36733452",R.drawable.ic_group));
        contactList.add(new Contact("D","36733452",R.drawable.ic_group));
        contactList.add(new Contact("E","36733452",R.drawable.ic_group));
        contactList.add(new Contact("F","36733452",R.drawable.ic_group));
        contactList.add(new Contact("G","36733452",R.drawable.ic_group));
        contactList.add(new Contact("H","36733452",R.drawable.ic_group));
        contactList.add(new Contact("I","36733452",R.drawable.ic_group));
        contactList.add(new Contact("J","36733452",R.drawable.ic_group));
    }
}*/





















//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class ContactFragment extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_contact, container, false);
//    }
//
//
//}
