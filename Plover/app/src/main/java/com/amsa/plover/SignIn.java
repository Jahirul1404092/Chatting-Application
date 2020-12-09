package com.amsa.plover;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText phoneNoSignIn,passwordSignIn;
    Button signIn,goSignUp,forgetPass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mAuth=FirebaseAuth.getInstance();
        phoneNoSignIn=(EditText) findViewById(R.id.phoneNoSignInEditTextId);
        passwordSignIn=(EditText) findViewById(R.id.passwordSignInEditTextId);
        signIn=(Button) findViewById(R.id.signInbuttonId);
        forgetPass=(Button) findViewById(R.id.goForgetPasswordId);
        goSignUp=(Button) findViewById(R.id.goSignUpbuttonId);
        progressBar=(ProgressBar)findViewById(R.id.progressbarSignin_Id);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
        actionBar.setIcon(R.mipmap.ic_launcher_logo);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String takenPhoneNo=phoneNoSignIn.getText().toString();
                final String password=passwordSignIn.getText().toString();
                if(takenPhoneNo.startsWith("+88")){
                    takenPhoneNo=takenPhoneNo.substring(3);
                }
                final String phoneNo=takenPhoneNo;
                ////////do work
                if(phoneNo.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill all filed",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    if (phoneNo.length() < 11) {
                        Toast.makeText(getApplicationContext(), "Phone no must be minimum 11 digit", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        //////////////do work
                        matchPhonePassword("+88" + phoneNo, password);
                        phoneNoSignIn.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                }

            }
        });

        goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gosignUpintent=new Intent(getApplicationContext(),SignUp.class);
                startActivity(gosignUpintent);
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////// do reset
//                Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
//                intent.putExtra("parent","SignIn");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    private void matchPhonePassword(final String mobile, final String password){

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Profile");
        databaseReference.orderByChild("Phone").equalTo(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                    /// ache

                    final DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Profile");
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i=0;
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Profile profile=dataSnapshot1.getValue(Profile.class);
//                    System.out.println("ssssss  "+profile.getId()+" "+profile.getName()+" "+profile.getGender()+" "+profile.getPhone()+" "+profile.getPassword()+" "+profile.getImageName()+" "+profile.getImageDownloadUrl());
                                if(mobile.equals(profile.getPhone()) && password.equals(profile.getPassword())){
                                    ////if match then go to home
                                    i=1;
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    intent.putExtra("profileId", profile.getId());//////sending profileId to home
                                    intent.putExtra("name", profile.getName());//////sending profileId to home
                                    intent.putExtra("mobile", profile.getPhone());//////sending profileId to home
                                    intent.putExtra("gender", profile.getGender());//////sending profileId to home
                                    intent.putExtra("imageName", profile.getImageName());//////sending profileId to home
                                    intent.putExtra("imageDownloadUrl", profile.getImageDownloadUrl());//////sending profileId to home
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                            }
                            if(i==0){
                                Toast.makeText(getApplicationContext(),"Not match",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else{
                    Toast.makeText(getApplicationContext(),mobile+" has no profile",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPreferences=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("profileId") && sharedPreferences.contains("name")&& sharedPreferences.contains("phone")&& sharedPreferences.contains("gender")&& sharedPreferences.contains("imageName")&& sharedPreferences.contains("imageDownloadUrl")) {
            String profileId = sharedPreferences.getString("profileId", null);
            String name = sharedPreferences.getString("name", null);
            String phone = sharedPreferences.getString("phone", null);
            String gender = sharedPreferences.getString("gender", null);
            String imageName = sharedPreferences.getString("imageName", null);
            String imageDownloadUrl = sharedPreferences.getString("imageDownloadUrl", null);
            System.out.println("hhhhhhhhhkk  " + name + " " + profileId + " " + phone + " " + gender + " " + imageName + " " + imageDownloadUrl);
            if (FirebaseAuth.getInstance().getCurrentUser() != null || profileId != null || name != null || phone != null || gender != null || imageName != null || imageDownloadUrl != null) {
                Intent intent = new Intent(SignIn.this, Home.class);
                intent.putExtra("profileId", profileId);//////sending profileId to home
                intent.putExtra("name", name);//////sending profileId to home
                intent.putExtra("mobile", phone);//////sending profileId to home
                intent.putExtra("gender", gender);//////sending profileId to home
                intent.putExtra("imageName", imageName);//////sending profileId to home
                intent.putExtra("imageDownloadUrl", imageDownloadUrl);//////sending profileId to home
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);

            }
        }
    }
}
