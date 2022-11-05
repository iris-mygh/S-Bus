package com.iot.sbus;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class QRActivity extends AppCompatActivity {

    ImageButton qr_camera_bnt;
    ImageView iv_qr_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        qr_camera_bnt = (ImageButton) findViewById(R.id.img_qrcamera);
        iv_qr_img = (ImageView) findViewById(R.id.qr_img);
        //int REQUESt_CODE_CAMERA = 123;

        Intent intent = getIntent();
        String qrValue = intent.getStringExtra("qrValue");
        if (qrValue != null && !qrValue.equals("")) {
            try {
                Toast.makeText(QRActivity.this, qrValue, Toast.LENGTH_SHORT).show();
                JSONObject jsnLocalData = new JSONObject(ApiPrivateFile.GetLocalData(QRActivity.this));
                jsnLocalData.put("qrvalue", qrValue);
                ApiPrivateFile.SaveLocalData(QRActivity.this, jsnLocalData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(QRActivity.this, MapActivity.class));
        }

        qr_camera_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(QRActivity.this, activity_qrscanner.class);
                QRActivity.this.startActivity(myIntent);
            }
        });

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