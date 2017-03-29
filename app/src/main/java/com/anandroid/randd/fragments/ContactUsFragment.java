package com.anandroid.randd.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.utils.Constants;
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
public class ContactUsFragment extends Fragment implements ServiceEvents {

    ProgressDialog pd;
    TextView address,mobile,email,timing,review;
    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_contact_us, container, false);
        address = (TextView)v.findViewById(R.id.address);
        mobile = (TextView)v.findViewById(R.id.mobile);
        email = (TextView)v.findViewById(R.id.email);
        review = (TextView)v.findViewById(R.id.review);
        timing = (TextView)v.findViewById(R.id.timing);
        doRegister();
        return v;
    }
    private void doRegister(){
        Utils.LogMessage(Constants.TAG, "About Us Request::> " + getDoRegisterRequest()  + "!!");
        if (Utils.isOnline(getActivity())) {
            GetServices bzzzLoginServices = new GetServices(ContactUsFragment.this, getActivity());
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
        params.put("tag", "contact_us");
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
                    address.setText(jsonObject.getJSONObject("responsedata").getString("address"));
                    mobile.setText(jsonObject.getJSONObject("responsedata").getString("mobile"));
                    email.setText(jsonObject.getJSONObject("responsedata").getString("email"));
                    timing.setText(jsonObject.getJSONObject("responsedata").getString("open"));
                    review.setText(jsonObject.getJSONObject("responsedata").getString("review"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Utils.showDialog(getActivity(), "",
                    "Response not getting from server");
        }

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

