package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        MaterialButton logout_btn = (MaterialButton)findViewById(R.id.bn_logout);
        // return Login page
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerActivity.this,LoginActivity.class));

            }


        });

        // book a bus
        MaterialButton bookbus_btn = (MaterialButton)findViewById(R.id.bn_bookbus);
        bookbus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerActivity.this,QRActivity.class));

            }


        });

    }
}