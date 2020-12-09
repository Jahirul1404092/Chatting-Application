package com.amsa.plover;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class Home extends AppCompatActivity implements DataProviderFromHomeActivity/* , View.OnClickListener */{
    private Button chatButton,contactButton,profileButton;
    private static String profileId,name,gender,mobile,imageName,imageDownloadUrl;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    public ViewPagerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private static int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout=(TabLayout) findViewById(R.id.tablayout_id);
        viewPager=(ViewPager) findViewById(R.id.viewpager_Id);
        adapter=new ViewPagerAdapter(getSupportFragmentManager());

        /// Add Fragment Here
        adapter.AddFragment(new ChatFragment(),"Chat");
        adapter.AddFragment(new ContactFragment(),"Friends");
        adapter.AddFragment(new RequestFragment(),"Request");
        adapter.AddFragment(new ProfileFragment(),"Profile");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_message);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_notification);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_account_circle);



        //remove Shadow from ActionBar
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setElevation(0);

        Intent intent=getIntent();
        new Home().profileId=intent.getStringExtra("profileId");
        new Home().name=intent.getStringExtra("name");
        new Home().mobile=intent.getStringExtra("mobile");
        new Home().gender=intent.getStringExtra("gender");
        new Home().imageName=intent.getStringExtra("imageName");
        new Home().imageDownloadUrl=intent.getStringExtra("imageDownloadUrl");
//        System.out.println("hhhhhhhhh  "+name+" "+profileId+" "+mobile+" "+gender+" "+imageName+" "+ imageDownloadUrl);
        SharedPreferences sharedPreferences=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("profileId",profileId);
        editor.putString("name",name);
        editor.putString("phone",mobile);
        editor.putString("gender",gender);
        editor.putString("imageName",imageName);
        editor.putString("imageDownloadUrl",imageDownloadUrl);
        editor.commit();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
        actionBar.setIcon(R.mipmap.ic_launcher_logo);

        chatButton=(Button)findViewById(R.id.chatButtonId);
        contactButton=(Button)findViewById(R.id.contactButtonId);
        profileButton=(Button)findViewById(R.id.profileButtonId);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeViewHomeId);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
////                Toast.makeText(getApplicationContext(),"swiping",Toast.LENGTH_SHORT).show();
//                onPause();
//                onStop();
//                onRestart();
//                onStart();
//                onResume();
                finish();
                startActivity(getIntent());
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);

        this.menu = menu;
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();

            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setQueryHint("Enter Number not name");

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(final String newText) {
//                    Toast.makeText(getApplicationContext(),"req sending",Toast.LENGTH_LONG).show();
                    if(newText.startsWith("+88")){Toast.makeText(getApplicationContext(),"Enter number without \"+88\" ",Toast.LENGTH_LONG).show();Toast.makeText(getApplicationContext(),"Enter number without \"+88\" ",Toast.LENGTH_SHORT).show();}
                    if(newText.length()>=11 && !(newText.startsWith("+88"))){



                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Profile");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                new Home().i=0;
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    final Profile profile=dataSnapshot1.getValue(Profile.class);
//                                    System.out.println("ssssss  "+profile.getId()+" "+profile.getName()+" "+profile.getGender()+" "+profile.getPhone()+" "+profile.getPassword()+" "+profile.getImageName()+" "+profile.getImageDownloadUrl());
                                    if(("+88"+newText).equals(profile.getPhone())){i=1;
                                        final Dialog mydialog;
                                        mydialog=new Dialog(Home.this);
                                        mydialog.setContentView(R.layout.dialogforsearch);
                                        TextView search_dialog_name_tv=(TextView) mydialog.findViewById(R.id.search_dialog_name_Id);
                                        TextView search_dialog_phone_tv=(TextView) mydialog.findViewById(R.id.search_dialog_phone_Id);
                                        ImageView search_dialog_contact_img=(ImageView)mydialog.findViewById(R.id.search_dialog_image_Id);
                                        final Button search_dialog_req_send_btn=(Button) mydialog.findViewById(R.id.search_dialog_req_send_btn_Id);
                                        final Button search_dialog_req_cancel_btn=(Button) mydialog.findViewById(R.id.search_dialog_req_cancel_btn_Id);
                                        final Button search_dialog_req_accept_btn=(Button) mydialog.findViewById(R.id.search_dialog_req_accept_btn_Id);

                                        search_dialog_name_tv.setText(profile.getName());
                                        search_dialog_phone_tv.setText(profile.getPhone());
                                        Picasso.with(mydialog.getContext()).load(profile.getImageDownloadUrl()).placeholder(R.drawable.male).fit().centerCrop().into(search_dialog_contact_img);

//                                            Toast.makeText(mydialog.getContext(),profile.getImageDownloadUrl(),Toast.LENGTH_LONG).show();
                                        try {
                                            mydialog.show();
                                        }
                                        catch (Exception e){
//                                            Toast.makeText(mydialog.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                        }
//
//                                        final DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Profile");
//                                        userRef.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                                                    Profile profile=dataSnapshot1.getValue(Profile.class);
//                                                    if(mobile.equals(profile.getPhone()) && password.equals(profile.getPassword())){
//                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                        startActivity(intent);
//                                                        finish();
//                                                        break;
//                                                    }
//                                                }
//                                                Toast.makeText(getApplicationContext(),"Not match",Toast.LENGTH_LONG).show();
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
                                        ////jodi ami pathai
                                        DatabaseReference userRef1=FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestSent");
                                        userRef1.orderByChild("Id").equalTo(profile.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue()!=null){
                                                    ///ache
//                                                    Toast.makeText(mydialog.getContext(),"you cant sent request",Toast.LENGTH_LONG).show();
                                                    search_dialog_req_accept_btn.setEnabled(false);
                                                    search_dialog_req_cancel_btn.setEnabled(true);
                                                    search_dialog_req_send_btn.setEnabled(false);

                                                }
                                                else{
                                                    ///nai
//                                                    Toast.makeText(mydialog.getContext(),"can sent request",Toast.LENGTH_LONG).show();
//                                                    search_dialog_req_accept_btn.setEnabled(false);
//                                                    search_dialog_req_cancel_btn.setEnabled(false);
//                                                    search_dialog_req_send_btn.setEnabled(true);
                                                    //// jodi se pathay
                                                    DatabaseReference userRef2=FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestCome");
                                                    userRef2.orderByChild("Id").equalTo(profile.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.getValue()!=null){
                                                                ///ache
//                                                                Toast.makeText(mydialog.getContext(),"request asche",Toast.LENGTH_LONG).show();
                                                                search_dialog_req_accept_btn.setEnabled(true);
                                                                search_dialog_req_cancel_btn.setEnabled(true);
                                                                search_dialog_req_send_btn.setEnabled(false);

                                                            }
                                                            else{

                                                                //// jodi already friend hoy
                                                                DatabaseReference userRef4=FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("Friends");
                                                                userRef4.orderByChild("Id").equalTo(profile.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if(dataSnapshot.getValue()!=null){
                                                                            ///ache
//                                                                            Toast.makeText(mydialog.getContext(),"This number is already your friend",Toast.LENGTH_LONG).show();
                                                                            search_dialog_req_accept_btn.setEnabled(false);
                                                                            search_dialog_req_cancel_btn.setEnabled(false);
                                                                            search_dialog_req_send_btn.setEnabled(false);

                                                                        }
                                                                        else{
//                                                                Toast.makeText(mydialog.getContext(),"request asenai",Toast.LENGTH_LONG).show();
                                                                            search_dialog_req_accept_btn.setEnabled(false);
                                                                            search_dialog_req_cancel_btn.setEnabled(false);
                                                                            search_dialog_req_send_btn.setEnabled(true);
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });

                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        search_dialog_req_send_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //////////send add request

                                                HashMap<String,String> hashMap1=new HashMap<>();
                                                hashMap1.put("Id",profileId);
                                                hashMap1.put("Name",name);
                                                hashMap1.put("Gender",gender);
                                                hashMap1.put("Phone",mobile);
                                                hashMap1.put("ImageName",imageName);
                                                hashMap1.put("ImageDownloadUrl",imageDownloadUrl);

//                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("Friends").child(profile.getId());
                                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Profile").child(profile.getId()).child("RequestCome").child(profileId);
                                                databaseReference1.setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if(task1.isSuccessful()){

                                                        }
                                                    }
                                                });
                                                HashMap<String,String> hashMap2=new HashMap<>();
                                                hashMap2.put("Id",profile.getId());
                                                hashMap2.put("Name",profile.getName());
                                                hashMap2.put("Gender",profile.getGender());
                                                hashMap2.put("Phone",profile.getPhone());
//                                                hashMap2.put("Password",profile.getPassword());
                                                hashMap2.put("ImageName",profile.getImageName());
                                                hashMap2.put("ImageDownloadUrl",profile.getImageDownloadUrl());
                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestSent").child(profile.getId());
                                                databaseReference2.setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if(task1.isSuccessful()){
                                                            Toast.makeText(mydialog.getContext(),"request Sent",Toast.LENGTH_LONG).show();
                                                            mydialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                        });



                                        search_dialog_req_cancel_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //////////request cancel jodi nije pathai
                                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Profile").child(profile.getId()).child("RequestCome").child(profileId);
                                                databaseReference1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqCome theke delete kore
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if(task1.isSuccessful()){

                                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestSent").child(profile.getId());
                                                            databaseReference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {/////nijer reqSent theke delete kora
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task1) {
                                                                    if(task1.isSuccessful()){
                                                                        Toast.makeText(mydialog.getContext(),"request canceled",Toast.LENGTH_LONG).show();
                                                                        mydialog.dismiss();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                });


                                                //////////request cancel jodi onno keu pathay
                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestCome").child(profile.getId());
                                                databaseReference3.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {/////nijer reqCome theke delete kore
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if(task1.isSuccessful()){

                                                            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Profile").child(profile.getId()).child("RequestSent").child(profileId);
                                                            databaseReference4.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqSent theke delete kora
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task1) {
                                                                    if(task1.isSuccessful()){
                                                                        Toast.makeText(mydialog.getContext(),"request canceled",Toast.LENGTH_LONG).show();
                                                                        mydialog.dismiss();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                });




                                            }
                                        });

                                        search_dialog_req_accept_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ////accept request
                                                String key=profileId+"+";
                                                key += profile.getId();
                                                final String msgId=String.valueOf(Calendar.getInstance().getTimeInMillis());
                                                HashMap<String,String> hashMap3=new HashMap<>();
                                                hashMap3.put("Msg","Welcome");
                                                hashMap3.put("MsgId",profileId);
                                                hashMap3.put("MsgType","text");
                                                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference("Friendship").child(key).child(msgId);
                                                databaseReference5.setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {/////friend directory te fnf directory baniye
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if(task1.isSuccessful()){

                                                            String key=profile.getId()+"+";
                                                            key += profileId;
                                                            HashMap<String,String> hashMap10=new HashMap<>();
                                                            hashMap10.put("Msg","Welcome");
                                                            hashMap10.put("MsgId",profile.getId());
                                                            hashMap10.put("MsgType","text");
                                                            DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference("Friendship").child(key).child(msgId);
                                                            databaseReference10.setValue(hashMap10).addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqSent theke delete kora
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task1) {
                                                                    if(task1.isSuccessful()){

                                                                    }
                                                                }
                                                            });


                                                            DatabaseReference databaseReference6 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestCome").child(profile.getId());
                                                            databaseReference6.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {/////nijer reqCome theke delete kore
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task1) {
                                                                    if(task1.isSuccessful()){

                                                                        HashMap<String,String> hashMap8=new HashMap<>();
                                                                        hashMap8.put("Id",profileId);
                                                                        hashMap8.put("Name",name);
                                                                        hashMap8.put("Gender",gender);
                                                                        hashMap8.put("Phone",mobile);
                                                                        hashMap8.put("Password","notUsed");
                                                                        hashMap8.put("ImageName",imageName);
                                                                        hashMap8.put("ImageDownloadUrl",imageDownloadUrl);
                                                                        DatabaseReference databaseReference8 = FirebaseDatabase.getInstance().getReference("Profile").child(profile.getId()).child("Friends").child(profileId);
                                                                        databaseReference8.setValue(hashMap8).addOnCompleteListener(new OnCompleteListener<Void>() {//tar friend lis e nijeke include kora
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task1) {////tar friends directory te amake add kora
                                                                                if(task1.isSuccessful()){

                                                                                }
                                                                            }
                                                                        });
                                                                        HashMap<String,String> hashMap9=new HashMap<>();
                                                                        hashMap9.put("Id",profile.getId());
                                                                        hashMap9.put("Name",profile.getName());
                                                                        hashMap9.put("Gender",profile.getGender());
                                                                        hashMap9.put("Phone",profile.getPhone());
                                                                        hashMap9.put("Password","notUsed");
                                                                        hashMap9.put("ImageName",profile.getImageName());
                                                                        hashMap9.put("ImageDownloadUrl",profile.getImageDownloadUrl());
                                                                        DatabaseReference databaseReference9 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("Friends").child(profile.getId());
                                                                        databaseReference9.setValue(hashMap9).addOnCompleteListener(new OnCompleteListener<Void>() {//nijer friend lis e take include kora
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task1) {////amar friends directory te take add kora
                                                                                if(task1.isSuccessful()){
                                                                                    Toast.makeText(mydialog.getContext(),"request Sent",Toast.LENGTH_LONG).show();
                                                                                    mydialog.dismiss();
                                                                                }
                                                                            }
                                                                        });

                                                                        DatabaseReference databaseReference7 = FirebaseDatabase.getInstance().getReference("Profile").child(profile.getId()).child("RequestSent").child(profileId);
                                                                        databaseReference7.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqSent theke delete kora
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task1) {
                                                                                if(task1.isSuccessful()){

                                                                                    Toast.makeText(mydialog.getContext(),"request accepted",Toast.LENGTH_LONG).show();
                                                                                    mydialog.dismiss();
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                                if(i!=1){
                                    Toast.makeText(getApplicationContext(),"No profile! ",Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }




                    return false;
                }
            });



        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            case R.id.action_refresh_home_Id:
                //refresh
                finish();
                startActivity(getIntent());
                return true;

            case R.id.action_profile:
                /////show req list
                Fragment fragment;
                fragment=new ProfileFragment();
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewpager_Id,fragment);
                fragmentTransaction.commit();


                return true;

            case R.id.action_web:
                // go to web
                Intent intent1 =new Intent(getApplicationContext(),About.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                return true;

            case R.id.action_contactus:
                // go to contact page
//                Toast.makeText(getApplicationContext(),"Contact",Toast.LENGTH_LONG).show();
                Intent intent2 =new Intent(getApplicationContext(),ContactUs.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                return true;

            case R.id.action_signout:
                // sign out
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences=getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
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

//    @Override
//    public void onClick(View v) {
//        if(v.getId()==R.id.chatButtonId){
//            Fragment fragment;
//            fragment=new ChatFragment();
//            FragmentManager fragmentManager=getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragmentId,fragment);
//            fragmentTransaction.commit();
//        }
//
//        else if(v.getId()==R.id.contactButtonId){
//            Fragment fragment;
//            fragment=new ContactFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId,fragment).commit();
//        }
//        else if(v.getId()==R.id.profileButtonId){
//            Fragment fragment;
//            fragment=new ProfileFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId,fragment).commit();
//        }
//    }
@Override
public void onBackPressed(){
    AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
    builder.setTitle("Exit!!")
            .setMessage("Are you sure?")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Home.super.onBackPressed();
                }
            })
            .setNegativeButton("Cancel",null)
            .setCancelable(false);
    AlertDialog alert=builder.create();
    alert.show();
}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProfileId() {
        return profileId;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public String getMobile() {

        return mobile;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public String getImageDownloadUrl() {
        return imageDownloadUrl;
    }
}
