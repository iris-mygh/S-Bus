package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView phonenumber =(TextView) findViewById(R.id.et_phone);
        TextView password =(TextView) findViewById(R.id.et_pass);
        TextView repassword =(TextView) findViewById(R.id.et_rewrite_pass);

        MaterialButton register_btn = (MaterialButton) findViewById(R.id.register_paccount);
        TextView login_btn = (TextView) findViewById(R.id.tv_returnlogin);


//        0982345678 - 12345678 - 12345678
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phonenumber.getText().toString().equals("0982345678") && password.getText().toString().equals(repassword.getText().toString())){
                    //correct
                    Toast.makeText(RegisterActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                }else
                    //incorrect
                    Toast.makeText(RegisterActivity.this,"Đăng ký tài khoản thất bại !!!",Toast.LENGTH_SHORT).show();
            }
        });

        // Return Login page
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

            }


        });
    }
}
