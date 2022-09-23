package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NextBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_bus);
        TextView tvTimer;
        tvTimer=(TextView)findViewById(R.id.tv_timer);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        tvTimer.setText(date);

        // return Login page
        MaterialButton logout_btn = (MaterialButton)findViewById(R.id.bn_logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NextBusActivity.this,LoginActivity.class));

            }


        });
    }
}