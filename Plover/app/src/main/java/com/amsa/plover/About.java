package com.amsa.plover;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class About extends AppCompatActivity {
    private Button gohome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        gohome=(Button)findViewById(R.id.gohome_about_Id);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, Home.class);
                intent.putExtra("profileId",new Home().getProfileId());//////sending profileId to home
                intent.putExtra("name",new Home().getName());//////sending profileId to home
                intent.putExtra("mobile",new Home().getMobile());//////sending profileId to home
                intent.putExtra("gender",new Home().getGender());//////sending profileId to home
                intent.putExtra("imageName",new Home().getImageName());//////sending profileId to home
                intent.putExtra("imageDownloadUrl",new Home().getImageDownloadUrl());//////sending profileId to home
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
