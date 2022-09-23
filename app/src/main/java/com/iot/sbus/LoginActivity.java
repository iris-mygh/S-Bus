package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        TextView forgotpass_btn = (TextView) findViewById(R.id.forgotpass);

        // Register
        TextView tv_regis= (TextView) findViewById(R.id.tv_register);
        tv_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }


        });

//        Driver: 0902345678 - 12345678
//        Customer: 0902345678 - 12345678
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("0902345678") && password.getText().toString().equals("12345678")) {
                    //correct: Driver
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,DriverActivity.class));
                } else if (username.getText().toString().equals("0912345678") && password.getText().toString().equals("12345678")) {
                    //correct: Customer
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công !!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,CustomerActivity.class));
                } else {
                    //incorrect
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    // Forgot Password
        forgotpass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassActivity.class));

            }


        });
    }
}
