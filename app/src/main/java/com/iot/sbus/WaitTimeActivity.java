package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WaitTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_time);

        TextView tvTimer;
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        tvTimer.setText(date);


        MaterialButton verifyup_btn = (MaterialButton) findViewById(R.id.bn_verifyup);
        // You have successfully boarded the car
        verifyup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WaitTimeActivity.this, "Chào mừng bạn đã lên xe", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WaitTimeActivity.this, EndTimeActivity.class));

            }
        });

    }
}