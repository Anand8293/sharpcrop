package com.anandroid.randd.web;

import android.content.Context;
import android.util.Log;

import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by HP 15 on 09-03-2016.
 */
public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private static final String FILE_PART_NAME = "profileimage";
    private static final String STRING_PART_NAME = "text";

    private final Response.Listener<String> mListener;
    private File mFilePart;
    Context context;
    Map<String, String> map;
    private long cacheTimeToLive = 5;


    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener, File file,
                            Map<String, String> map,Context context) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        this.context = context;
        this.map = map;
        uploadEventImage(file);
    }

    private void uploadEventImage(File file) {

        try {
                for(Map.Entry entry : map.entrySet()) {
                entity.addPart((String) entry.getKey(), new StringBody((String) entry.getValue()));
                entity.addPart("photo", new FileBody(file));
            }

        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }



    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        Log.e(Constants.TAG,"Reqest => "+bos.toString());
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {

            // Volley does not handle null properly, so implement null response
            // check
            if (response.data.length == 0) {
                byte[] responseData = "{}".getBytes("UTF8");
                response = new NetworkResponse(response.statusCode,
                        responseData, response.headers, response.notModified);
            }

            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            return Response.success(jsonString,
                    parseIgnoreCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            System.out.println("VolleyQueue: Encoding Error for " + getTag()
                    + " (" + getSequence() + ")");
            return Response.error(new ParseError(e));
        }
        // return Response.success("Test", getCacheEntry());
    }

    public Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        serverEtag = headers.get("ETag");

        final long cacheHitButRefreshed = 15 * 60 * 1000; //Fifteen Minutes
        final long cacheExpired = cacheTimeToLive;
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}