package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EndTimeActivity extends AppCompatActivity {
    JSONObject jsnlocalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_time);

        TextView tvTimer;
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        tvTimer.setText(date);

        try {
            jsnlocalData = new JSONObject(ApiPrivateFile.GetLocalData(EndTimeActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Verify successfully
        MaterialButton verifyup_btn = (MaterialButton) findViewById(R.id.bn_verifyup);
        verifyup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EndTimeActivity.this, "Bạn đã lên xe thành công", Toast.LENGTH_SHORT).show();
                try {
                    UpdateTicket(jsnlocalData.getString("id_ticket"), "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        // Verify not successfully
        MaterialButton verifynotup_btn = (MaterialButton) findViewById(R.id.bn_verifynotup);
        verifynotup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EndTimeActivity.this, "Vui lòng chọn lại Điểm đến và Tuyến xe", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EndTimeActivity.this, MapActivity.class));

            }
        });

    }

    private void UpdateTicket(String strTicketId, String strStatus) {
        ApiCaller ac = new ApiCaller(EndTimeActivity.this,
                "updateTicketStatus",
                "id_ticket=" + strTicketId + "&&confirm=" + strStatus,
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"id":"0"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                Toast.makeText(EndTimeActivity.this, "Xác nhận thành công. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EndTimeActivity.this, CustomerActivity.class));

                            }
                            else {
                                Toast.makeText(EndTimeActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(EndTimeActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EndTimeActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

}