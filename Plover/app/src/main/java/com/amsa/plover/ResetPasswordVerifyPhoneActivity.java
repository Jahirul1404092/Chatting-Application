package com.amsa.plover;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ResetPasswordVerifyPhoneActivity extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;
    private TextView textView;

    //firebase auth object
    private FirebaseAuth mAuth;

    //    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ProgressBar progressBar;
    private static String mobile;
    private static String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_verify_phone);
        progressBar=(ProgressBar)findViewById(R.id.progressbarResetPassVerifyCodeId);


        //initializing objects
        mAuth = FirebaseAuth.getInstance();
//        auth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCodeResetPassVerifyCodeId);
        textView=findViewById(R.id.textViewResetPassVerifyCodeId);
//        FirebaseAuth.getInstance().signOut();     ///for sign out

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        ResetPasswordVerifyPhoneActivity obj=new ResetPasswordVerifyPhoneActivity();

        Intent intent = getIntent();
        obj.mobile = intent.getStringExtra("mobile");
        obj.password = intent.getStringExtra("password");
//        System.out.println("lllllllll "+mobile+" "+password);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
        actionBar.setIcon(R.mipmap.ic_launcher_logo);

        sendVerificationCode(mobile);



        textView.setText("Code is Sent to \n"+mobile);


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.codeSendButtonResetPassVerifyCodeId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                v.setBackgroundColor(Color.GRAY);
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    progressBar.setVisibility(View.GONE);
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    v.setBackgroundColor(Color.BLUE);
                    return;
                }

                verifyVerificationCode(code);
                v.setEnabled(false);
            }
        });

    }

    //the method is sending verification codD
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ResetPasswordVerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ResetPasswordVerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            ///Authintication successful
                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userid=firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(userid).child("Password");
                            databaseReference.setValue(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task1) {
                                    if(task1.isSuccessful()){
                                        Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"New Password is set",Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(getApplicationContext(), VerifyPhoneActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Something is Wrong try again",Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }
                                }
                            });
//                            final DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Profile");
//                            userRef.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                                        Profile profile=dataSnapshot1.getValue(Profile.class);
////                    System.out.println("ssssss  "+profile.getId()+" "+profile.getName()+" "+profile.getGender()+" "+profile.getPhone()+" "+profile.getPassword()+" "+profile.getImageName()+" "+profile.getImageDownloadUrl());
//                                        if(mobile.equals(profile.getPhone())){
//                                            /////set pass
////                                            System.out.println("lllllllll "+mobile+" "+password+" "+profile.getId());
//                                            databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(userid).child("Password");
//                                            databaseReference.setValue(password).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task1) {
//                                                    if(task1.isSuccessful()){
//                                                        Intent intent = new Intent(getApplicationContext(), SignIn.class);
//                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                        progressBar.setVisibility(View.GONE);
//                                                        Toast.makeText(getApplicationContext(),"New Password is set",Toast.LENGTH_LONG).show();
//                                                        startActivity(intent);
//                                                    }else {
//                                                        Intent intent = new Intent(getApplicationContext(), VerifyPhoneActivity.class);
//                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                        progressBar.setVisibility(View.GONE);
//                                                        Toast.makeText(getApplicationContext(),"Something is Wrong try again",Toast.LENGTH_LONG).show();
//                                                        startActivity(intent);
//                                                    }
//                                                }
//                                            });
//                                            break;
//                                        }
//                                    }
//
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });

                        } else {

                            //verification unsuccessful.. display an error message
                            progressBar.setVisibility(View.GONE);
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressBar.setVisibility(View.GONE);
                                message = "Invalid code entered...";
                            }
//                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
////                                Toast.makeText(getApplicationContext(),"User is already Reagisterred",Toast.LENGTH_LONG).show();
//                                progressBar.setVisibility(View.GONE);
//                                message="User is already registered";
//                            }

//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            findViewById(R.id.codeSendButtonResetPassVerifyCodeId).setEnabled(true);
                            findViewById(R.id.codeSendButtonResetPassVerifyCodeId).setBackgroundColor(Color.BLUE);
                        }
                    }
                });
    }

}
