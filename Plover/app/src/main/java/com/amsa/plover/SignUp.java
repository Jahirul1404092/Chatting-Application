package com.amsa.plover;

import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class SignUp extends AppCompatActivity {
    private EditText nameSignUp,phonNoSignup,passwordSignUp,repasswordSignUp;
    private RadioButton genderMale,genderFemale;
    private RadioGroup genderGroup;
    public Button signUp,goSignIn;
//    private static String gender;
    private ImageView profileImage;
    private Uri imageUri;
    private static final int IMAGE_REQUEST = 1;
    private RadioButton rb;
    StorageTask uploadTask;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        nameSignUp=(EditText)findViewById(R.id.nameSignUpEditTextId);
        phonNoSignup=(EditText)findViewById(R.id.phoneSignUpEditTextId);
        passwordSignUp=(EditText)findViewById(R.id.passSignUpEditTextId);
        repasswordSignUp=(EditText)findViewById(R.id.repassSignUpEditTextId);
        genderMale=(RadioButton)findViewById(R.id.genderMaleId);
        genderFemale=(RadioButton)findViewById(R.id.genderFemaleId);
        genderGroup=(RadioGroup)findViewById(R.id.genderSignUpRadioGroupId);
        signUp=(Button)findViewById(R.id.signUpbuttonId);
        goSignIn=(Button)findViewById(R.id.goSignInButtonIdFP);
        profileImage=(ImageView)findViewById(R.id.imageSignUpImageViewId);
        progressBar=(ProgressBar)findViewById(R.id.progressBarsignupId);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
        actionBar.setIcon(R.mipmap.ic_launcher_logo);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String name = nameSignUp.getText().toString();
                String takenPhoneNo= phonNoSignup.getText().toString();
                final String password = passwordSignUp.getText().toString();
                final String repassword = repasswordSignUp.getText().toString();
                if(takenPhoneNo.startsWith("+88")){
                    takenPhoneNo=takenPhoneNo.substring(3);
                }
                final String phoneNo=takenPhoneNo;

                String gender;
//                int i = genderGroup.getCheckedRadioButtonId();
//                new SignUp().rb = (RadioButton) genderGroup.findViewById(i);
//                try {
//                    new SignUp().gender = rb.getText().toString();
//
//                } catch (Exception ex) {
//                    new SignUp().gender = "Male";
//                }
                if (genderFemale.isChecked()){
                    gender="Female";
                }
                else {
                    gender="Male";
                }
                if(imageUri!=null){
                    Picasso.with(SignUp.this).load(imageUri).into(profileImage);
                }



                if (name.isEmpty() || phoneNo.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all filed", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    if(phoneNo.length()<11 || (phoneNo.startsWith("+") && phoneNo.length()<14)||phoneNo.length()>=14 ||phoneNo.contains("!")||phoneNo.contains("@")||phoneNo.contains("#")||phoneNo.contains("$")||phoneNo.contains("%")||phoneNo.contains("^")||phoneNo.contains("&")||phoneNo.contains("*")||phoneNo.contains("(")||phoneNo.contains(")")||phoneNo.contains("_")||phoneNo.contains("-")||phoneNo.contains("/")||phoneNo.contains("?")||phoneNo.contains(">")||phoneNo.contains("<")||phoneNo.contains(",")||phoneNo.contains(".")||phoneNo.contains("\"")||phoneNo.contains("|")||phoneNo.contains("{")||phoneNo.contains("}")||phoneNo.contains(";")||phoneNo.contains("]")||phoneNo.contains("[")||phoneNo.contains(":")){
                        Toast.makeText(getApplicationContext(), "Enter Valid phone no", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else if (!password.equals(repassword)) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Re Enter the same password", Toast.LENGTH_LONG).show();
                    } else if (password.length() < 6 || repassword.length() < 6) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Password must be at least 6 digit", Toast.LENGTH_LONG).show();
                    } else {
                        //////////////do work
                        Intent intent = new Intent(SignUp.this, VerifyPhoneActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("mobile", phoneNo);
                        intent.putExtra("gender",gender);
                        intent.putExtra("password", password);
                        if(imageUri==null){
                            intent.putExtra("imageUri",gender);
                        }
                        else{
                            intent.putExtra("imageUri", imageUri.toString());
                        }

                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                    }
                }

            }
        });

        goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignInintent=new Intent(getApplicationContext(),SignIn.class);
                startActivity(goSignInintent);
            }
        });


    }
    public void openFileChooser(){

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
//            Toast.makeText(getApplicationContext(),imageUri.toString(),Toast.LENGTH_LONG).show();
            Picasso.with(this).load(imageUri).into(profileImage);
        }
        else if(data==null || data.getData()==null){
//            if(rb.getText().toString()=="Male"){
//                imageUri = Uri.parse("android.resource://com.example.chatapp/mipmap/ic_action_add_person.png");
//            }
//            else if(rb.getText().toString()=="Female") {
//                imageUri = Uri.parse("android.resource://com.example.chatapp/mipmap/ic_action_person.png");
//            }
//            else{
//                imageUri = Uri.parse("android.resource://com.example.chatapp/mipmap/ic_action_add_person.png");
//            }

//            Picasso.with(this).load(imageUri).into(profileImage);
            try {
                InputStream stream = getContentResolver().openInputStream(imageUri);
            }
            catch (Exception e){

            }


        }
    }
}
