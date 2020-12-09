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
import android.widget.Button;
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

public class VerifyPhoneActivity extends AppCompatActivity {

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
    StorageTask uploadTask;

    private static String name;
    private static String mobile;
    private static String gender;
    private static String password;
    private static Uri imageUri;
    private ProgressBar progressBar;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        VerifyPhoneActivity ob=new VerifyPhoneActivity();
        progressBar=(ProgressBar)findViewById(R.id.progressbarVerifycodeId);
        send=(Button)findViewById(R.id.verificationCodeSendButtonId);
        send.setEnabled(false);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
//        auth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        textView= findViewById(R.id.textView);
//        FirebaseAuth.getInstance().signOut();     ///for sign out

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        ob.name = intent.getStringExtra("name");
        ob.mobile = intent.getStringExtra("mobile");
        ob.gender = intent.getStringExtra("gender");
        ob.password = intent.getStringExtra("password");
        ob.imageUri = Uri.parse(intent.getStringExtra("imageUri"));
//        Toast.makeText(getApplicationContext(),imageUri.toString(),Toast.LENGTH_LONG).show();
//        sendVerificationCode(mobile);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
        actionBar.setIcon(R.mipmap.ic_launcher_logo);

        isRegistered(mobile);

        textView.setText("Code is Sent to \n"+mobile);
        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        send.setOnClickListener(new View.OnClickListener() {
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
//                if(uploadTask!=null && uploadTask.isInProgress()){
//                    Toast.makeText(getApplicationContext(),"Upload is in progress",Toast.LENGTH_LONG).show();
//                    findViewById(R.id.verificationCodeSendButtonId).setEnabled(false);
//                }
//                else {
////                    //verifying the code entered manually
//                    findViewById(R.id.verificationCodeSendButtonId).setEnabled(true);
//                }
            }
        });
    }
    private void isRegistered(final String mobile){
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("Profile");
        userRef.orderByChild("Phone").equalTo("+88"+mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(),mobile+" is already registered",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                }
                else{
                    send.setEnabled(true);
                    sendVerificationCode(mobile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + mobile,
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
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userid=firebaseUser.getUid();
                            ////code verification is Successfull
//                            System.out.println("hhhhhhhhhh "+imageUri.toString());

                            if(imageUri.toString().equals("Male")){
//                                System.out.println("hhhhhhhhhh Male");
                                ////going to store profile info
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("Id",userid);
                                hashMap.put("Name",name);
                                hashMap.put("Gender",gender);
                                hashMap.put("Phone","+88"+mobile);
                                hashMap.put("Password",password);
                                hashMap.put("ImageName","+88"+mobile+".jpg");
                                hashMap.put("ImageDownloadUrl","https://firebasestorage.googleapis.com/v0/b/chatapp-34c8a.appspot.com/o/Profile%2Fmale.png?alt=media&token=241a80fd-718e-4d78-9700-ad98e527b6ec");


                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(userid);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if(task1.isSuccessful()){
                                            //verification successful we will start the profile activity
                                            Intent intent = new Intent(VerifyPhoneActivity.this, Home.class);
                                            intent.putExtra("profileId",userid);//////sending profileId to home
                                            intent.putExtra("name",name);//////sending profileId to home
                                            intent.putExtra("mobile","+88"+mobile);//////sending profileId to home
                                            intent.putExtra("gender",gender);//////sending profileId to home
                                            intent.putExtra("imageName","+88"+mobile+".jpg");//////sending profileId to home
                                            intent.putExtra("imageDownloadUrl","https://firebasestorage.googleapis.com/v0/b/chatapp-34c8a.appspot.com/o/Profile%2Fmale.png?alt=media&token=241a80fd-718e-4d78-9700-ad98e527b6ec");//////sending profileId to home
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(intent);
                                            findViewById(R.id.verificationCodeSendButtonId).setEnabled(true);
                                            findViewById(R.id.verificationCodeSendButtonId).setBackgroundColor(Color.BLUE);

                                        }
                                    }
                                });
                            }
                            else if(imageUri.toString().equals("Female"))
                            {
                                ////going to store profile info
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("Id",userid);
                                hashMap.put("Name",name);
                                hashMap.put("Gender",gender);
                                hashMap.put("Phone","+88"+mobile);
                                hashMap.put("Password",password);
                                hashMap.put("ImageName","+88"+mobile+".jpg");
                                hashMap.put("ImageDownloadUrl","https://firebasestorage.googleapis.com/v0/b/chatapp-34c8a.appspot.com/o/Profile%2Ffemale.jpg?alt=media&token=3b135852-b0da-4cdf-980d-b5c83bc520dd");


                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(userid);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if(task1.isSuccessful()){
                                            //verification successful we will start the profile activity
                                            Intent intent = new Intent(VerifyPhoneActivity.this, Home.class);
                                            intent.putExtra("profileId",userid);//////sending profileId to home
                                            intent.putExtra("name",name);//////sending profileId to home
                                            intent.putExtra("mobile","+88"+mobile);//////sending profileId to home
                                            intent.putExtra("gender",gender);//////sending profileId to home
                                            intent.putExtra("imageName","+88"+mobile+".jpg");//////sending profileId to home
                                            intent.putExtra("imageDownloadUrl","https://firebasestorage.googleapis.com/v0/b/chatapp-34c8a.appspot.com/o/Profile%2Ffemale.jpg?alt=media&token=3b135852-b0da-4cdf-980d-b5c83bc520dd");//////sending profileId to home
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(intent);
                                            findViewById(R.id.verificationCodeSendButtonId).setEnabled(true);
                                            findViewById(R.id.verificationCodeSendButtonId).setBackgroundColor(Color.BLUE);

                                        }
                                    }
                                });
                            }
                            else{

                                ////MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(imageUri)).toString()  this thing finds image extension from image Uri
                                storageReference= FirebaseStorage.getInstance().getReference("Profile");
                                final String Extension="."+MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(imageUri)).toString();
                                StorageReference ref=storageReference.child("+88"+mobile+Extension);
                                ref.putFile(imageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Get a URL to the uploaded content
                                                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                                                while(!urlTask.isSuccessful());
                                                final Uri downloadUrl=urlTask.getResult();
                                                Toast.makeText(getApplicationContext(),"Image is Stored Successfully",Toast.LENGTH_LONG).show();

                                                ////going to store profile info
                                                HashMap<String,String> hashMap=new HashMap<>();
                                                hashMap.put("Id",userid);
                                                hashMap.put("Name",name);
                                                hashMap.put("Gender",gender);
                                                hashMap.put("Phone","+88"+mobile);
                                                hashMap.put("Password",password);
                                                hashMap.put("ImageName","+88"+mobile+Extension);
                                                hashMap.put("ImageDownloadUrl",downloadUrl.toString());


                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(userid);
                                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if(task1.isSuccessful()){
                                                            //verification successful we will start the profile activity
                                                            Intent intent = new Intent(VerifyPhoneActivity.this, Home.class);
                                                            intent.putExtra("profileId",userid);//////sending profileId to home
                                                            intent.putExtra("name",name);//////sending profileId to home
                                                            intent.putExtra("mobile","+88"+mobile);//////sending profileId to home
                                                            intent.putExtra("gender",gender);//////sending profileId to home
                                                            intent.putExtra("imageName","+88"+mobile+Extension);//////sending profileId to home
                                                            intent.putExtra("imageDownloadUrl",downloadUrl.toString());//////sending profileId to home
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            progressBar.setVisibility(View.GONE);
                                                            startActivity(intent);
                                                            findViewById(R.id.verificationCodeSendButtonId).setEnabled(true);
                                                            findViewById(R.id.verificationCodeSendButtonId).setBackgroundColor(Color.BLUE);

                                                        }
                                                    }
                                                });

//                                                        UploadedProfileImage uploadedProfileImage = new UploadedProfileImage(imageName,downloadUrl.toString());
//
//                                                        String uploadId=databaseReference.push().getKey();
//                                                        databaseReference.child(uploadId).setValue(upload);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                // ...
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(),"Image is not Stored Successfully",Toast.LENGTH_LONG).show();
                                                findViewById(R.id.verificationCodeSendButtonId).setEnabled(true);
                                                findViewById(R.id.verificationCodeSendButtonId).setBackgroundColor(Color.BLUE);
                                            }
                                        });
                            }





//

                        } else {

                            //verification unsuccessful.. display an error message
                            progressBar.setVisibility(View.GONE);
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressBar.setVisibility(View.GONE);
                                message = "Invalid code entered...";
                            }
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
//                                Toast.makeText(getApplicationContext(),"User is already Reagisterred",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                message="User is already registered";
                            }

//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            findViewById(R.id.verificationCodeSendButtonId).setEnabled(true);
                            findViewById(R.id.verificationCodeSendButtonId).setBackgroundColor(Color.BLUE);
                        }
                    }
                });
    }

}
