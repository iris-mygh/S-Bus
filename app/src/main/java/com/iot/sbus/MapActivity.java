package com.iot.sbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    JSONObject jsnlocalData;
    List<String> lsStation;
    List<String> lsBus;
    Spinner spnStation;
    Spinner spnBusNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        spnStation =(Spinner) findViewById(R.id.spnStation);
        spnBusNo =(Spinner) findViewById(R.id.spnBusNo);

        try {
            jsnlocalData = new JSONObject(ApiPrivateFile.GetLocalData(MapActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InitStationList();

        // Verify route bus
        Button verifytrip_btn = (Button) findViewById(R.id.bn_verifytrip);
        verifytrip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jsnlocalData.put("station", lsStation.get(spnStation.getSelectedItemPosition()));
                    jsnlocalData.put("bus", lsBus.get(spnBusNo.getSelectedItemPosition()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ApiPrivateFile.SaveLocalData(MapActivity.this, jsnlocalData.toString());
                checkEndBusDay();
            }
        });

    }

    private void InitStationList() {
        // Get list of station
        ApiCaller ac = new ApiCaller(MapActivity.this,
                "busstationlist",
                "",
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"id_bus_station":"1","station_name":"[BX 06] Công Trường Mê Linh"},{"id_bus_station":"2","station_name":"[Q1 031] Bến Bạch Đằng"},{"id_bus_station":"3","station_name":"[Q1 020] Cục Hải Quan Thành Phố"},{"id_bus_station":"4","station_name":"[Q1 193] Chợ Cũ"},{"id_bus_station":"5","station_name":"[Q1 194] Trường Cao Thắng"},{"id_bus_station":"6","station_name":"[Q1 102] Trạm Trung chuyển trên đường Hàm Nghi"},{"id_bus_station":"7","station_name":"[Q1 196] Bảo tàng Mỹ Thuật"},{"id_bus_station":"8","station_name":"[Q1 124] Trường Ernst Thalmann"},{"id_bus_station":"9","station_name":"[Q1 125] KTX Trần Hưng Đạo"},{"id_bus_station":"10","station_name":"[Q1 126] Rạp Hưng Đạo"},{"id_bus_station":"11","station_name":"[Q1 127] Bệnh viện Răng Hàm Mặt"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                int intSelectedIndex = 0;
                                lsStation = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    lsStation.add(data.getJSONObject(i).getString("id_bus_station")
                                            + " - " + data.getJSONObject(i).getString("station_name"));
                                    if (jsnlocalData.has("qrvalue") && data.getJSONObject(i).getString("id_bus_station").equals(jsnlocalData.getString("qrvalue").split("_")[1])) {
                                        intSelectedIndex = i;
                                    }
                                }

                                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>((Context)MapActivity.this, android.R.layout.simple_spinner_item, lsStation.toArray(new String[]{}));
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spnStation.setAdapter(adapter);
                                spnStation.setSelection(intSelectedIndex);
                                spnStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        Toast.makeText(MapActivity.this, "" + lsStation.get(i) + " Selected..", Toast.LENGTH_SHORT).show();
                                        InitBusRouteList(lsStation.get(i).split(" - ")[0]);

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                            else {
                                Toast.makeText(MapActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MapActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void InitBusRouteList(String strBusStationId) {
        // Get list of bus route
        ApiCaller ac = new ApiCaller(MapActivity.this,
                "routelist",
                "id_bus_station=" + strBusStationId,
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"list_id":"3","list_bus_name":"[70-2] - Hợp tác xã vận tải 19/5"},{"list_id":"4","list_bus_name":"[150] - Hợp tác xã vận tải 19/5"},{"list_id":"1","list_bus_name":"[103] - Công ty Cổ phần Xe khách Sài Gòn"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            lsBus = new ArrayList<>();
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    lsBus.add(data.getJSONObject(i).getString("list_id")
                                            + " - " + data.getJSONObject(i).getString("list_bus_name"));
                                }

                            }
                            else {
                                Toast.makeText(MapActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>((Context)MapActivity.this, android.R.layout.simple_spinner_item, lsBus.toArray(new String[]{}));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnBusNo.setAdapter(adapter);
                            spnBusNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(MapActivity.this, "" + lsBus.get(i) + " Selected..", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        } catch (JSONException e) {
                            Toast.makeText(MapActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

    private void checkEndBusDay() {
        // Get list of bus route
        ApiCaller ac = new ApiCaller(MapActivity.this,
                "checkEndBusDay",
                "",
                new ApiCaller.funcCallBackCls() {
                    @Override
                    public void onResponse(String response) {
                        //{"status":"OK","message":"Success","data":[{"total_trip":"1"}]}
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray data = res.getJSONArray("data");
                            if (data.length() > 0) {
                                if (data.getJSONObject(0).getString("total_trip").equals("0")) {
                                    Toast.makeText(MapActivity.this, "Đã hết chuyến " + jsnlocalData.getString("bus"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MapActivity.this, "Xác nhận chuyến đi thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MapActivity.this, WaitTimeActivity.class));
                                }
                            }
                            else {
                                Toast.makeText(MapActivity.this, "Không có dữ liệu trả về. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MapActivity.this, "Đã có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "Đã có lỗi xảy ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

}