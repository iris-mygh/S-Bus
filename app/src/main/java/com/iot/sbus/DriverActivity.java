package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.busicon);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setContentView(R.layout.activity_driver);

        TextView tvTimer;
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        tvTimer.setText(date);

        // Next station
        MaterialButton bn_nextstation = (MaterialButton) findViewById(R.id.bn_arrived_station);
        bn_nextstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DriverActivity.this,NextBusActivity.class));

            }
        });

        // return Login page
        MaterialButton logout_btn = (MaterialButton) findViewById(R.id.bn_logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DriverActivity.this,LoginActivity.class));

            }


        });

    }
}