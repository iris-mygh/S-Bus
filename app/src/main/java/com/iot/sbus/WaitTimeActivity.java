package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WaitTimeActivity extends AppCompatActivity {
    JSONObject jsnlocalData;
    int intTktRemainTime;
    TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_time);

        tvTimer = (TextView) findViewById(R.id.tv_timer);
//        DateFormat df = new SimpleDateFormat("HH:mm");
//        String date = df.format(Calendar.getInstance().getTime());
//        tvTimer.setText(date);

        try {
            jsnlocalData = new JSONObject(ApiPrivateFile.GetLocalData(WaitTimeActivity.this));
            AddTicket(jsnlocalData.getString("station").split("-")[0]
                    , jsnlocalData.getString("bus").split("-")[0]
                    , jsnlocalData.getString("id_user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button verifyup_btn = (Button) findViewById(R.id.bn_verifyup);
        // You have successfully boarded the car
        verifyup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    UpdateTicket(jsnlocalData.getString("id_ticket"), "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void AddTicket(String strStationId, String strBusId, String strUserId) {
        ApiCaller ac = new ApiCaller(WaitTimeActivity.this,
                "addTicket",
                "id_bus_station=" + strStationId + "&&id_user=" + strUserId + "&&id_bus=" + strBusId,
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"id":"18"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                Toast.makeText(WaitTimeActivity.this, "Tạo ticket thành công. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                                GetTicket(data.getJSONObject(0).getString("id"));
                            }
                            else {
                                Toast.makeText(WaitTimeActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(WaitTimeActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WaitTimeActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void GetTicket(String strTicketId) {
        ApiCaller ac = new ApiCaller(WaitTimeActivity.this,
                "getTicket",
                "id_ticket=" + strTicketId,
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"id_ticket":"18","ticket_end_time":"2022-10-24 23:07:30","remain_time":"698"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                jsnlocalData.put("id_ticket", data.getJSONObject(0).getString("id_ticket"));
                                ApiPrivateFile.SaveLocalData(WaitTimeActivity.this, jsnlocalData.toString());

                                intTktRemainTime = Integer.parseInt(data.getJSONObject(0).getString("remain_time"));
                                CountDownTime();
                            }
                            else {
                                Toast.makeText(WaitTimeActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(WaitTimeActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WaitTimeActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void UpdateTicket(String strTicketId, String strStatus) {
        ApiCaller ac = new ApiCaller(WaitTimeActivity.this,
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
                                Toast.makeText(WaitTimeActivity.this, "Xác nhận thành công. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(WaitTimeActivity.this, EndTimeActivity.class));
                            }
                            else {
                                Toast.makeText(WaitTimeActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(WaitTimeActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WaitTimeActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void CountDownTime() {
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                intTktRemainTime--;
                tvTimer.setText(
                        (intTktRemainTime / 60 < 10 ? "0" : "") + (intTktRemainTime / 60)
                        + ":" +
                        (intTktRemainTime % 60 < 10 ? "0" : "") + (intTktRemainTime % 60));
                if (intTktRemainTime > 0) {
                    h.postDelayed(this, 1000);
                } else {
                    Toast.makeText(WaitTimeActivity.this, "Vui lòng xác nhận tình trạng lên xe. ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WaitTimeActivity.this, EndTimeActivity.class));
                }
            }
        }, 1000);
    }

}