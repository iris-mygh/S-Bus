package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                // https://baongo0056.xyz/?api=login&&phone_number=0967123456&&password=abc123
                // {"status":"OK","message":"Success","data":[{"id_user":"1","full_name":"Nguyen Van A","user_type":"1","first_char":"A"}]}
                ApiCaller ac = new ApiCaller(LoginActivity.this,
                        "login",
                        "phone_number=" + username.getText() + "&&password=" + password.getText(),
                        new ApiCaller.funcCallBackCls() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject res = new JSONObject(response);
                                    JSONArray data = res.getJSONArray("data");
                                    if (data.length() > 0) {
                                        //correct: Customer
                                        if (data.getJSONObject(0).getString("user_type").equals("1")) {
                                            Toast.makeText(LoginActivity.this, "Đăng nhập hành khách thành công", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,CustomerActivity.class));
                                        }
                                        //correct: Driver
                                        else if (data.getJSONObject(0).getString("user_type").equals("2")) {
                                            Toast.makeText(LoginActivity.this, "Đăng nhập tài xế thành công", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,DriverActivity.class));
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng kiểm tra thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                                        }

                                        ApiPrivateFile.SaveLocalData(LoginActivity.this, data.getJSONObject(0).toString());
                                        //Toast.makeText(LoginActivity.this, ApiPrivateFile.GetLocalData(LoginActivity.this), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                ac.run();
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
