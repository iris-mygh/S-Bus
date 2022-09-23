package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        // Verify route bus
        MaterialButton verifytrip_btn = (MaterialButton) findViewById(R.id.bn_verifytrip);
        verifytrip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "Xác nhận chuyến đi thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MapActivity.this, WaitTimeActivity.class));

            }
        });

    }
}