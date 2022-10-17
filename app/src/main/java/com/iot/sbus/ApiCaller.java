package com.iot.sbus;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ApiCaller {
    private String apiUrl = "";
    private AppCompatActivity app;
    private String api = "";
    private String paramStr = "";
    private funcCallBackCls funcCallBack;

    ApiCaller(AppCompatActivity app, String api, String paramStr, funcCallBackCls funcCallBack) {
        this.app = app;
        this.api = api;
        this.paramStr = paramStr;
        this.funcCallBack = funcCallBack;
        this.apiUrl = app.getResources().getString(R.string.api_url);
    }

    public void run() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(app);
        String url = apiUrl + String.format("?api=%1$s&&%2$s", api, paramStr);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        funcCallBack.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                funcCallBack.onErrorResponse(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public interface funcCallBackCls {
        public void onResponse(String response);
        public void onErrorResponse(VolleyError error);
    }
}
