package com.anandroid.randd.sharpcrop;

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
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends BaseActivity implements ServiceEvents {

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setClick(R.id.send_otp);
    }

    private Map getOTPConfirmRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "otp");
        params.put("email", UserPrefs.getUserUniqueId(this));
        params.put("Otp", getTextViewText(R.id.otp_et).toString().trim());
        return params;
    }

    private void getOTPConfirm() {
        Utils.LogMessage(Constants.TAG, "OTP Request::> " + getOTPConfirmRequest()
                + "!!");
        if (Utils.isOnline(this)) {
            GetServices bzzzLoginServices = new GetServices(OTPActivity.this, this);
            try {
                bzzzLoginServices.RestCallAsync("", getOTPConfirmRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(OTPActivity.this, "",
                    getString(R.string.no_network));
    }

    @Override
    public void StartedRequest() {
        pd = ProgressDialog.show(OTPActivity.this, "",
                getString(R.string.pls_wait));
        pd.setCancelable(false);
    }

    @Override
    public void Finished(String methodName, Object Data) {
        if (pd != null)
            pd.dismiss();
        String res = (String) Data;
        Gson gson= new Gson();

        Log.e(Constants.TAG, "Country List Response::> " + res);
        if (res.trim().length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String msg = jsonObject1.getString("success");
                Toast.makeText(this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();
                if (msg.equalsIgnoreCase("1")) {

                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(this, RegisterActivity.class));
                    finish();
                }
                    Toast.makeText(this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.showDialog(OTPActivity.this, "",
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_otp:
                if(!isEmpty())
                    getOTPConfirm();
                break;

            default:
                break;

        }
    }

    private boolean isEmpty() {
        boolean empty = false;
        if (getTextViewText(R.id.otp_et).toString().trim().length() == 0) {
            Utils.showDialog(OTPActivity.this, "", "Enter OTP");
            empty = true;
        }

        return empty;
    }

}
