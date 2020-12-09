package com.amsa.plover;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class ResetPassword extends AppCompatActivity {
    private EditText phonNoResetPass,passwordResetPass,repasswordResetPass;
    private Button reset,goSignInResetPass;
    private Uri imageUri;
    private ProgressBar progressBarResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
        actionBar.setIcon(R.mipmap.ic_launcher_logo);


        phonNoResetPass=(EditText)findViewById(R.id.phoneResetPassEditTextId);
        passwordResetPass=(EditText)findViewById(R.id.passResetPassEditTextId);
        repasswordResetPass=(EditText)findViewById(R.id.repassResetPassEditTextId);
        goSignInResetPass=(Button)findViewById(R.id.goSignInButtonResetPass_Id);
        progressBarResetPass=(ProgressBar)findViewById(R.id.progressBarResetPassId);
        reset=(Button)findViewById(R.id.resetbuttonResetPassId);



        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBarResetPass.setVisibility(View.VISIBLE);
                String takenPhoneNo= phonNoResetPass.getText().toString();
                final String password = passwordResetPass.getText().toString();
                final String repassword = repasswordResetPass.getText().toString();
                if(takenPhoneNo.startsWith("+88")){
                    takenPhoneNo=takenPhoneNo.substring(3);
                }
                final String phoneNo="+88"+takenPhoneNo;
                if ( phoneNo.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all filed", Toast.LENGTH_LONG).show();
                    progressBarResetPass.setVisibility(View.GONE);
                }
                else {
                    if(phoneNo.length()<11 || (phoneNo.startsWith("+") && phoneNo.length()<14) ||phoneNo.length()>14 ||phoneNo.contains("!")||phoneNo.contains("@")||phoneNo.contains("#")||phoneNo.contains("$")||phoneNo.contains("%")||phoneNo.contains("^")||phoneNo.contains("&")||phoneNo.contains("*")||phoneNo.contains("(")||phoneNo.contains(")")||phoneNo.contains("_")||phoneNo.contains("-")||phoneNo.contains("/")||phoneNo.contains("?")||phoneNo.contains(">")||phoneNo.contains("<")||phoneNo.contains(",")||phoneNo.contains(".")||phoneNo.contains("\"")||phoneNo.contains("|")||phoneNo.contains("{")||phoneNo.contains("}")||phoneNo.contains(";")||phoneNo.contains("]")||phoneNo.contains("[")||phoneNo.contains(":")){
                        Toast.makeText(getApplicationContext(), "Enter Valid phone no", Toast.LENGTH_LONG).show();
                        progressBarResetPass.setVisibility(View.GONE);
                    }
                    else if (!password.equals(repassword)) {
                        progressBarResetPass.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Re Enter the same password", Toast.LENGTH_LONG).show();
                    } else if (password.length() < 6 || repassword.length() < 6) {
                        progressBarResetPass.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Password must be at least 6 digit", Toast.LENGTH_LONG).show();
                    } else {
                        //////////////do work

                        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Profile");
                        databaseReference.orderByChild("Phone").equalTo(phoneNo).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null){

                                    /// ache
                                    Intent intent = new Intent(ResetPassword.this, ResetPasswordVerifyPhoneActivity.class);
                                    intent.putExtra("mobile", phoneNo);
                                    intent.putExtra("password", password);
//                                    System.out.println("lllllllllRRR "+phoneNo+" "+password);
                                    progressBarResetPass.setVisibility(View.GONE);
                                    startActivity(intent);


                                }
                                else{
                                    Toast.makeText(getApplicationContext(),phoneNo+" has no profile",Toast.LENGTH_SHORT).show();
                                    progressBarResetPass.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

            }
        });

        goSignInResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignInintent=new Intent(getApplicationContext(),SignIn.class);
                startActivity(goSignInintent);
                finish();
            }
        });


    }

}
