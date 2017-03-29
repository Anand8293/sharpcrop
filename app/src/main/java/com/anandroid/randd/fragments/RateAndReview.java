package com.anandroid.randd.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.anandroid.randd.adopter.ServicesAdopter;
import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.anandroid.randd.web.GetServices;
import com.anandroid.randd.web.ServiceEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateAndReview extends Fragment implements ServiceEvents {

    RatingBar rb;
    Button submit;
    EditText comment;
    ProgressDialog pd;

    public RateAndReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_rate_and_review, container, false);
        rb = (RatingBar)v.findViewById(R.id.ratingBar1);
        submit = (Button) v.findViewById(R.id.submit);
        comment = (EditText) v.findViewById(R.id.comment);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRateAndReview();
            }
        });
        return v;
    }
    private void setRateAndReview(){
        Utils.LogMessage(Constants.TAG, "Services Request::> " + setDoRateAndReview()  + "!!");
        if (Utils.isOnline(getActivity())) {
            GetServices bzzzLoginServices = new GetServices(RateAndReview.this, getActivity());
            try {
                bzzzLoginServices.RestCallAsync("", setDoRateAndReview());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(getActivity(), "",
                    getString(R.string.no_network));
    }
    private Map setDoRateAndReview() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "rating_review_insert");
        params.put("star",String.valueOf(rb.getRating()));
        params.put("description", comment.getText().toString());
        params.put("email", UserPrefs.getUserUniqueId(getActivity()));
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
                    Utils.showDialog(getActivity(), "", "Thanks for Rating US");
                    getFragmentManager().beginTransaction().replace(R.id.frame,new ContentFragment()).commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Utils.showDialog(getActivity(), "", "Response not getting from server");
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
