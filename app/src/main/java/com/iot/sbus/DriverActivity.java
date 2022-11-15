package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DriverActivity extends AppCompatActivity {
    JSONObject jsnlocalData;
    TextView tv_numberbus;
    TextView tv_startpoint_name;
    TextView endpoint_name;
    TextView tv_presentstation_name;
    TextView tv_nextstation_name;
    TextView tv_countup_number;
    TextView tv_countonbus_number;
    TextView tv_countdown_number;
    TextView tv_timer;
    int intTktRemainTime;
    Handler intervalTime = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.busicon);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setContentView(R.layout.activity_driver);

        try {
            jsnlocalData = new JSONObject(ApiPrivateFile.GetLocalData(DriverActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_numberbus = (TextView) findViewById(R.id.tv_numberbus);
        tv_startpoint_name = (TextView) findViewById(R.id.tv_startpoint_name);
        endpoint_name = (TextView) findViewById(R.id.endpoint_name);
        tv_presentstation_name = (TextView) findViewById(R.id.tv_presentstation_name);
        tv_nextstation_name = (TextView) findViewById(R.id.tv_nextstation_name);
        tv_countup_number = (TextView) findViewById(R.id.tv_countup_number);
        tv_countonbus_number = (TextView) findViewById(R.id.tv_countonbus_number);
        tv_countdown_number = (TextView) findViewById(R.id.tv_countdown_number);

        tv_timer = (TextView) findViewById(R.id.tv_timer);

        tv_countup_number.setText("0");
        tv_countonbus_number.setText("Unknow");
        tv_countdown_number.setText("Unknow");

        try {
            InitDriverSchedule();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        TextView tvTimer;
//        tvTimer = (TextView) findViewById(R.id.tv_timer);
//        DateFormat df = new SimpleDateFormat("HH:mm");
//        String date = df.format(Calendar.getInstance().getTime());
//        tvTimer.setText(date);

        // Next station
        MaterialButton bn_nextstation = (MaterialButton) findViewById(R.id.bn_arrived_station);
        bn_nextstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(DriverActivity.this,NextBusActivity.class));
                try {
                    String strIdSchedule = jsnlocalData.getString("id_schedule");
                    UpdateDriverSchedule(strIdSchedule);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // return Login page
        MaterialButton logout_btn = (MaterialButton) findViewById(R.id.bn_logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DriverActivity.this,LoginActivity.class));

            }


        });


    }

    private void InitDriverSchedule() throws JSONException {
        // Get list of station
        ApiCaller ac = new ApiCaller(DriverActivity.this,
                "getDriverSchedule",
                "id_user=" + jsnlocalData.getString("id_user"),
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"id_schedule":"3","route_name":"Bến Thành - Trường Cao Thắng","bus_name":"Bus Number 103","main_start":" Công Trường Mê Linh","main_destination":" Trường Cao Thắng","sub_start":" Cục Hải Quan Thành Phố","sub_destination":" Chợ Cũ"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                jsnlocalData.put("id_schedule", data.getJSONObject(0).getString("id_schedule"));
                                jsnlocalData.put("id_bus_station", data.getJSONObject(0).getString("id_bus_station"));
                                tv_numberbus.setText(data.getJSONObject(0).getString("bus_name"));
                                tv_startpoint_name.setText(data.getJSONObject(0).getString("main_start"));
                                endpoint_name.setText(data.getJSONObject(0).getString("main_destination"));
                                tv_presentstation_name.setText(data.getJSONObject(0).getString("sub_start"));
                                tv_nextstation_name.setText(data.getJSONObject(0).getString("sub_destination"));

                                intTktRemainTime = 300;
                                CountDownTime();

                            }
                            else {
                                Toast.makeText(DriverActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(DriverActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DriverActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void UpdateDriverSchedule(String strId_schedule) {
        ApiCaller ac = new ApiCaller(DriverActivity.this,
                "updateDriverSchedule",
                "id_schedule=" + strId_schedule,
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"id":"0"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                Toast.makeText(DriverActivity.this, "Xác nhận thành công. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                                InitDriverSchedule();
                            }
                            else {
                                Toast.makeText(DriverActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(DriverActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DriverActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void GetCustomerCount() throws JSONException {
        // Get list of station
        ApiCaller ac = new ApiCaller(DriverActivity.this,
                "getCustomerCount",
                "id_bus_station=" + jsnlocalData.getString("id_bus_station"),
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"total_passenger":"0"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                tv_countup_number.setText(data.getJSONObject(0).getString("total_passenger"));
                            }
                            else {
                                Toast.makeText(DriverActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(DriverActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DriverActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void CountDownTime() {
        intervalTime.removeCallbacksAndMessages(null);
        intervalTime.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                intTktRemainTime--;
                tv_timer.setText(
                        (intTktRemainTime / 60 < 10 ? "0" : "") + (intTktRemainTime / 60)
                                + ":" +
                                (intTktRemainTime % 60 < 10 ? "0" : "") + (intTktRemainTime % 60));
                if (intTktRemainTime > 0) {
                    intervalTime.postDelayed(this, 1000);
                } else {
//                    Toast.makeText(DriverActivity.this, "Vui lòng xác nhận tình trạng lên xe. ", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(DriverActivity.this, EndTimeActivity.class));
                }

                if (String.valueOf(intTktRemainTime % 10).equals(DriverActivity.this.getResources().getString(R.string.interval))) {
                    try {
                        GetCustomerCount();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000);
    }

}