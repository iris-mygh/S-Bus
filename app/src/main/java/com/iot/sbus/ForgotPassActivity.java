package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class ForgotPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        TextView phonenumber =(TextView) findViewById(R.id.et_phonerg);
        TextView newpassword =(TextView) findViewById(R.id.et_newpass);
        TextView renewpassword =(TextView) findViewById(R.id.et_rewrite_newpass);

        MaterialButton changepass_btn = (MaterialButton) findViewById(R.id.changepass);
        TextView register_btn = (TextView) findViewById(R.id.tv_register);
        TextView login_btn = (TextView) findViewById(R.id.tv_return_login);

        // Register
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassActivity.this,RegisterActivity.class));

            }


        });

//        0912345678 - 12345678
        changepass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phonenumber.getText().toString().equals("0912345678") && newpassword.getText().toString().equals(renewpassword.getText().toString())){
                    //correct
                    Toast.makeText(ForgotPassActivity.this,"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                }else
                    //incorrect
                    Toast.makeText(ForgotPassActivity.this,"Đổi mật khẩu thất bại !!!",Toast.LENGTH_SHORT).show();
            }
        });

        // return Login page
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassActivity.this,LoginActivity.class));

            }


        });
    }
}
