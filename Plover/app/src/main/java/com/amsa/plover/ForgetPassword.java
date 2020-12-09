package com.amsa.plover;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {
    private TextView numAlredyRegistered;
    private Button goSignIn,resetPassword;
    private static String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);      //////////For Back Button
        actionBar.setIcon(R.mipmap.ic_launcher_logo);

        Intent intent = getIntent();
        new ForgetPassword().mobile = intent.getStringExtra("mobile");

        goSignIn=(Button)findViewById(R.id.goSignInButtonIdFP);
        resetPassword=(Button)findViewById(R.id.resetPasswordbuttonId);
        numAlredyRegistered=(TextView) findViewById(R.id.numAlreadyRegisteredTextView);

        if(mobile==null){
            numAlredyRegistered.setText("Forget Password?");
        }
        else{
            numAlredyRegistered.setText("+88"+mobile+ " has already an profile");
        }


        goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////do reset
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
