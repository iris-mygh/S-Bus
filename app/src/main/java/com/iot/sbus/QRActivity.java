package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        MaterialButton verifystation_btn = (MaterialButton) findViewById(R.id.bn_verifystation);
        // Verify bus station -> Verify bus route
        verifystation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QRActivity.this, "Xác nhận trạm xe thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(QRActivity.this, MapActivity.class));

            }
        });



        }
}