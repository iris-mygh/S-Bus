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
                        //{"status":"OK","message":"Success","data":[{"id_bus_station":"1","station_name":"[BX 06] C??ng Tr?????ng M?? Linh"},{"id_bus_station":"2","station_name":"[Q1 031] B???n B???ch ?????ng"},{"id_bus_station":"3","station_name":"[Q1 020] C???c H???i Quan Th??nh Ph???"},{"id_bus_station":"4","station_name":"[Q1 193] Ch??? C??"},{"id_bus_station":"5","station_name":"[Q1 194] Tr?????ng Cao Th???ng"},{"id_bus_station":"6","station_name":"[Q1 102] Tr???m Trung chuy???n tr??n ???????ng H??m Nghi"},{"id_bus_station":"7","station_name":"[Q1 196] B???o t??ng M??? Thu???t"},{"id_bus_station":"8","station_name":"[Q1 124] Tr?????ng Ernst Thalmann"},{"id_bus_station":"9","station_name":"[Q1 125] KTX Tr???n H??ng ?????o"},{"id_bus_station":"10","station_name":"[Q1 126] R???p H??ng ?????o"},{"id_bus_station":"11","station_name":"[Q1 127] B???nh vi???n R??ng H??m M???t"}]}
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
                                Toast.makeText(MapActivity.this, "Kh??ng c?? d??? li???u tr??? v???. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MapActivity.this, "???? c?? l???i x???y ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "???? c?? l???i x???y ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        //{"status":"OK","message":"Success","data":[{"list_id":"3","list_bus_name":"[70-2] - H???p t??c x?? v???n t???i 19/5"},{"list_id":"4","list_bus_name":"[150] - H???p t??c x?? v???n t???i 19/5"},{"list_id":"1","list_bus_name":"[103] - C??ng ty C??? ph???n Xe kh??ch S??i G??n"}]}
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
                                Toast.makeText(MapActivity.this, "Kh??ng c?? d??? li???u tr??? v???. " + res.getString("message"), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MapActivity.this, "???? c?? l???i x???y ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "???? c?? l???i x???y ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(MapActivity.this, "???? h???t chuy???n " + jsnlocalData.getString("bus"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MapActivity.this, "X??c nh???n chuy???n ??i th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MapActivity.this, WaitTimeActivity.class));
                                }
                            }
                            else {
                                Toast.makeText(MapActivity.this, "Kh??ng c?? d??? li???u tr??? v???. " + res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MapActivity.this, "???? c?? l???i x???y ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "???? c?? l???i x???y ra: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ac.run();
    }

}