package com.amsa.plover;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
//import android.os.Handler;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Messaging extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public ViewPagerAdapter adapter;
//    Handler mHandler;

    private static String fid;
    private static String fname;
    private static String fmobile;
    private static String fgender;
    private static String fpassword;
    private static String fimageUri;

    private static String profileId,name,gender,mobile,imageName,imageDownloadUrl;

    private ImageView myIcon;
    private ImageView msgSendbtn;
    private ImageView msgImageSendbtn;
    private EditText msgEditText;
//    Handler mHandler;
//    Runnable m_Runnable;
    Dialog mydialog;
    private long t1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

//        this.mHandler = new Handler();
//        this.mHandler.postDelayed(m_Runnable,5000);

        Messaging obj=new Messaging();
        msgSendbtn=(ImageView) findViewById(R.id.msgSendImageButtonId);
        msgImageSendbtn=(ImageView) findViewById(R.id.msgImageSendImageButtonId);
        msgEditText=(EditText)findViewById(R.id.messageEditText_Id);
        new Messaging().profileId=new Home().getProfileId();
        new Messaging().name=new Home().getName();
        new Messaging().mobile=new Home().getMobile();
        new Messaging().imageName=new Home().getImageName();
        new Messaging().imageDownloadUrl=new Home().getImageDownloadUrl();
        new Messaging().profileId=new Home().getProfileId();




        tabLayout=(TabLayout) findViewById(R.id.message_tablayout_id);
        viewPager=(ViewPager) findViewById(R.id.messaging_viewpager_Id);
        adapter=new ViewPagerAdapter(getSupportFragmentManager());

        /// Add Fragment Here
        adapter.AddFragment(new MessageFragment(),"Reload");
//        adapter.AddFragment(new ContactFragment(),"");
//        adapter.AddFragment(new RequestFragment(),"");
//        adapter.AddFragment(new ProfileFragment(),"");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_message);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_notification);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_account_circle);

        Intent intent = getIntent();
        obj.fid = intent.getStringExtra("id");
        obj.fname = intent.getStringExtra("name");
        obj.fmobile = intent.getStringExtra("mobile");
        obj.fgender = intent.getStringExtra("gender");
        obj.fpassword = intent.getStringExtra("password");
        obj.fimageUri = intent.getStringExtra("imageUri");
//        System.out.println("mmmmmmmmmmmmmmmmmmmmm   "+fid+"  "+fgender+"  "+fname+"  "+fmobile+"  "+fpassword+"  "+fimageUri);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//////for backbutton
////        getSupportActionBar().setDisplayUseLogoEnabled(true);
////        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.mipmap.ic_action_person);
//        Picasso.with(getApplicationContext()).load(imageDownloadUrl).placeholder(R.mipmap.ic_action_person).fit().centerCrop().into(ActionBar());

        final View actionBarLayout = getLayoutInflater().inflate(R.layout.custom_action_bar, null);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
//        actionBar.setIcon(R.mipmap.ic_launcher_logo);
        myIcon = (ImageView) actionBarLayout.findViewById(R.id.myIcon);
        Picasso.with(this).load(fimageUri).into(myIcon);
        actionBar.setTitle(fname);


         ///Dialog ini
        mydialog=new Dialog(Messaging.this);
        mydialog.setContentView(R.layout.dialog_contact);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView dialog_name_tv=(TextView) mydialog.findViewById(R.id.dialog_name_Id);
                TextView dialog_phone_tv=(TextView) mydialog.findViewById(R.id.dialog_phone_Id);
                ImageView dialog_contact_img=(ImageView)mydialog.findViewById(R.id.dialog_image_Id);
                Button bt1=(Button)mydialog.findViewById(R.id.dialog_call_btn_Id);
                bt1.setEnabled(false);
                Button bt2=(Button)mydialog.findViewById(R.id.dialog_msg_btn_Id);
                bt2.setEnabled(false);

                dialog_name_tv.setText(fname);
                dialog_phone_tv.setText(fgender);
                Picasso.with(Messaging.this).load(fimageUri).placeholder(R.drawable.male).fit().centerCrop().into(dialog_contact_img);

//                Toast.makeText(getApplicationContext(),fimageUri,Toast.LENGTH_LONG).show();
                mydialog.show();
            }
        });


        // Reload current fragment
//        try{
//            Fragment frg = null;
//            frg = getSupportFragmentManager().findFragmentById(R.id.messaging_viewpager_Id);
//            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.detach(frg);
//            ft.attach(frg);
//            ft.commit();
//        }
//        catch (Exception e){}




        msgSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg=msgEditText.getText().toString();
                final String msgId=String.valueOf(Calendar.getInstance().getTimeInMillis());
                if(!msg.equals(" ") && msg.length()!=0) {
                    String key = profileId + "+";
                    key += fid;
                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("Msg", msg);
                    hashMap1.put("MsgId", profileId);
                    hashMap1.put("MsgType", "text");
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Friendship").child(key).child(msgId);
                    databaseReference1.setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqSent theke delete kora
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {

                            }
                        }
                    });
                    String fkey = fid + "+";
                    fkey += profileId;
                    HashMap<String, String> hashMap2 = new HashMap<>();
                    hashMap2.put("Msg", msg);
                    hashMap2.put("MsgId", profileId);
                    hashMap2.put("MsgType", "text");
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Friendship").child(fkey).child(msgId);
                    databaseReference2.setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqSent theke delete kora
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {

                            }
                        }
                    });
//                HashMap<String,String> hashMap3=new HashMap<>();
//                hashMap3.put("Password","Used");
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Profile").child(fid).child("Friends").child(profileId).child("Password");
                    databaseReference3.setValue("Used").addOnCompleteListener(new OnCompleteListener<Void>() {//tar friend lis e nijeke include kora
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {////tar friends directory te amake add kora
                            if (task1.isSuccessful()) {

                            }
                        }
                    });
//                HashMap<String,String> hashMap4=new HashMap<>();
//                hashMap4.put("Password","Used");
                    DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("Friends").child(fid).child("Password");
                    databaseReference4.setValue("Used").addOnCompleteListener(new OnCompleteListener<Void>() {//nijer friend lis e take include kora
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {////amar friends directory te take add kora
                            if (task1.isSuccessful()) {

                            }
                        }
                    });
                    msgEditText.setText("");
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        msgImageSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Messaging.this,"Not Activated yet!!",Toast.LENGTH_LONG).show();
            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.messaging_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            case R.id.action_refresh_messagingId:
                //refreash
                finish();
                startActivity(getIntent());
                return true;

            case R.id.action_signout_messagingId:
                // sign out
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent =new Intent(getApplicationContext(),SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    private final Runnable m_Runnable = new Runnable()
//    {
//        public void run()
//
//        {
////            Toast.makeText(refresh.this,"in runnable",Toast.LENGTH_SHORT).show();
//
//            Messaging.this.mHandler.postDelayed(m_Runnable, 5000);
//        }
//
//    };//runnable
    public String getFid() {
        return fid;
    }

    public String getFname() {
        return fname;
    }

    public String getFmobile() {
        return fmobile;
    }

    public String getFgender() {
        return fgender;
    }

    public String getFpassword() {
        return fpassword;
    }

    public Uri getFimageUri() {
        return Uri.parse(fimageUri);
    }
}
