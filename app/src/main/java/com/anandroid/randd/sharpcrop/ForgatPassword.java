package com.anandroid.randd.sharpcrop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.anandroid.randd.web.GetServices;
import com.anandroid.randd.web.ServiceEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgatPassword extends BaseActivity implements ServiceEvents{

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setClick(R.id.submit);
    }

    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submit:
                if(!isEmpty()){
                    doForgat();
                }
                break;
              default:
                break;

        }
    }

    private void doForgat(){
        Utils.LogMessage(Constants.TAG, "User Forgat Request::> " + getDoForgatRequest()
                + "!!");
        if (Utils.isOnline(this)) {
            GetServices bzzzLoginServices = new GetServices(ForgatPassword.this, this);
            try {
                bzzzLoginServices.RestCallAsync("", getDoForgatRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(ForgatPassword.this, "",
                    getString(R.string.no_network));
    }
    private Map getDoForgatRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "forget_password_email");
        params.put("email", getTextViewText(R.id.email).toString().trim());
        return params;
    }


    @Override
    public void StartedRequest() {
        pd = ProgressDialog.show(ForgatPassword.this, "",
                getString(R.string.pls_wait));
        pd.setCancelable(false);
    }

    @Override
    public void Finished(String methodName, Object Data) {
        if (pd != null)
            pd.dismiss();
        String res = (String) Data;

        Log.e(Constants.TAG, "Country List Response::> " + res);
        if (res.trim().length() > 0) {

            Log.e(Constants.TAG, "serviceState==1");
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String msg = jsonObject1.getString("success");
                if (msg.equalsIgnoreCase("1")) {
                    UserPrefs.setUserId(getTextViewText(R.id.email),this);
                    UserPrefs.otp =  jsonObject.getJSONObject("responsedata").getString("otp");
                    startActivity(new Intent(ForgatPassword.this, NewPasswordGenration.class));
                    finish();
                }
                else
                    Toast.makeText(this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Utils.showDialog(ForgatPassword.this, "",
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


    private boolean isEmpty() {
        boolean empty = false;
        if (getTextViewText(R.id.email).toString().trim().length() == 0) {
            Utils.showDialog(ForgatPassword.this, "", "Enter email");
            empty = true;
        }
        else if (!Utils.isValidEmail(getTextViewText(R.id.email)
                .toString().trim())) {
            Utils.showDialog(ForgatPassword.this, "", "Enter valid email");
            empty = true;
        }
        return empty;
    }


}
