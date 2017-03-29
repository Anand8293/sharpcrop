package com.anandroid.randd.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.anandroid.randd.sharpcrop.OTPActivity;
import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.sharpcrop.RegisterActivity;
import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.anandroid.randd.web.GetServices;
import com.anandroid.randd.web.ServiceEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements ServiceEvents {

    WebView webview;
    ProgressDialog pd;
    String summary = "";
    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_about_us, container, false);
        webview = (WebView)v.findViewById(R.id.webView);
        doRegister();
        return v;
    }

    private void doRegister(){
        Utils.LogMessage(Constants.TAG, "About Us Request::> " + getDoRegisterRequest()
                + "!!");
        if (Utils.isOnline(getActivity())) {
            GetServices bzzzLoginServices = new GetServices(AboutUsFragment.this, getActivity());
            try {
                bzzzLoginServices.RestCallAsync("", getDoRegisterRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(getActivity(), "",
                    getString(R.string.no_network));
    }
    private Map getDoRegisterRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "about_us");
        return params;
    }


    @Override
    public void StartedRequest() {
        pd = ProgressDialog.show(getActivity(), "",
                getString(R.string.pls_wait));
        pd.setCancelable(false);
    }

    @Override
    public void Finished(String methodName, Object Data) {
        if (pd != null)
            pd.dismiss();
        String res = (String) Data;

        if (res.trim().length() > 0) {

            Log.e(Constants.TAG, "serviceState==1");
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String msg = jsonObject1.getString("success");
                if (msg.equalsIgnoreCase("1")) {
                     summary = jsonObject.getJSONObject("responsedata").getString("content1").toString();
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Utils.showDialog(getActivity(), "",
                    "Response not getting from server");
        }
        webview.loadData("<html><body align='justify'>"+summary+"</body></html>", "text/html; charset=utf-8", "utf-8");
    }

    @Override
    public void FinishedWithException(Exception ex) {
        // TODO Auto-generated method stub
        if (pd != null)
            pd.dismiss();
    }

    @Override
    public void EndedRequest() {
        // TODO Auto-generated method stub
        if (pd != null)
            pd.dismiss();
    }

}
