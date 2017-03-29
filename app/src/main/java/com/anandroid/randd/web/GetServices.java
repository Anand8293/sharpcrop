package com.anandroid.randd.web;

import android.content.Context;
import android.util.Log;

import com.anandroid.randd.utils.Constants;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.File;
import java.util.Map;

public class GetServices {


    public String url = "http://sharpcrop.com/app/";
    public String TAG = "SharpCrop";
    public String method;
    public int timeOut = 120000;
    String res = "";
    public ServiceEvents eventHandler;
    public static double MAXMB = 1024 * 1024 * 2;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    Context context;

    public GetServices() {
    }

    public GetServices(ServiceEvents eventHandler, Context context) {
        this.eventHandler = eventHandler;
        this.context = context;
    }

    public GetServices(ServiceEvents eventHandler, String url) {
        this.eventHandler = eventHandler;
        this.url = url;
    }

    public GetServices(ServiceEvents eventHandler, String url,
                       int timeOutInSeconds) {
        this.eventHandler = eventHandler;
        this.url = url;
        this.setTimeOut(timeOutInSeconds);
    }

    public void setTimeOut(int seconds) {
        this.timeOut = seconds * 1000;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void RestCallAsync(final String method, final Map request) throws Exception

    {
        this.method=method;
        eventHandler.StartedRequest();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + method,rl,re ) {
            @Override
            protected Map<String, String> getParams() {   return request;     }        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }
    Response.ErrorListener re=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //	Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error mgs::> " + error.toString());
            eventHandler.EndedRequest();
            eventHandler.Finished("" + method, "");
        }
    };
    Response.Listener rl= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            eventHandler.EndedRequest();
            if (response != null) {
                Log.e(Constants.TAG, "Response is " + response.toString());
                eventHandler.Finished("" + method, response);
            }
        }
    };

    public void MultiPartRestCallAsync(final String method, final Map request, final File profile) throws Exception

    {
        this.method=method;
        eventHandler.StartedRequest();

        MultipartRequest  stringRequest = new MultipartRequest ( url + method,re,rl,profile,request,context);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }
}
