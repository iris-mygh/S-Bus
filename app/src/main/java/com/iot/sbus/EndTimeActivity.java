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

public class EndTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_time);

        TextView tvTimer;
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        tvTimer.setText(date);


        // Verify successfully
        MaterialButton verifyup_btn = (MaterialButton) findViewById(R.id.bn_verifyup);
        verifyup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EndTimeActivity.this, "Bạn đã lên xe thành công", Toast.LENGTH_SHORT).show();

            }
        });


        // Verify not successfully
        MaterialButton verifynotup_btn = (MaterialButton) findViewById(R.id.bn_verifynotup);
        verifynotup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EndTimeActivity.this, "Vui lòng chọn lại Điểm đến và Tuyến xe khác", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EndTimeActivity.this, MapActivity.class));

            }
        });

    }
}