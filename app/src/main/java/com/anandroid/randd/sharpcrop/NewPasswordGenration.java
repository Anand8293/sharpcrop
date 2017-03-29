package com.anandroid.randd.sharpcrop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

public class NewPasswordGenration extends BaseActivity implements ServiceEvents {

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_the_code);
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
                    Log.e("OTP","OTP Same => "+getEditTextText(R.id.varfCode)+" == "+UserPrefs.otp);
                    if(getEditTextText(R.id.varfCode).equals(UserPrefs.otp)) {
                        UserPrefs.otp="";
                        UserPrefs.setUserId("",this);
                        doRegister();
                    }
                    else
                        Utils.showDialog(NewPasswordGenration.this, "", "Invalide is OTP");
                }
                break;

            default:
                break;

        }
    }

    private void doRegister(){
        Utils.LogMessage(Constants.TAG, "User NewPass Request::> " + getDoRegisterRequest()
                + "!!");
        if (Utils.isOnline(this)) {
            GetServices bzzzLoginServices = new GetServices(NewPasswordGenration.this, this);
            try {
                bzzzLoginServices.RestCallAsync("", getDoRegisterRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(NewPasswordGenration.this, "",
                    getString(R.string.no_network));
    }
    private Map getDoRegisterRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "forget_password_otp");
        params.put("email", UserPrefs.getUserId(this));
        params.put("password", getTextViewText(R.id.newPass).toString().trim());
        params.put("token", UserPrefs.getDeviceToken(this));
        return params;
    }


    @Override
    public void StartedRequest() {
        pd = ProgressDialog.show(NewPasswordGenration.this, "",
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
                    startActivity(new Intent(NewPasswordGenration.this, LoginActivity.class));
                    finish();
                }
                else
                    Toast.makeText(this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Utils.showDialog(NewPasswordGenration.this, "",
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
        if (getTextViewText(R.id.varfCode).toString().trim().length() == 0) {
            Utils.showDialog(NewPasswordGenration.this, "", "Enter OTP Code");
            empty = true;
        }
       else if (getTextViewText(R.id.newPass).toString().trim().length() == 0) {
            Utils.showDialog(NewPasswordGenration.this, "", "Enter New Password");
            empty = true;
        }  else if (getTextViewText(R.id.newRepass).toString().trim().length() == 0) {
            Utils.showDialog(NewPasswordGenration.this, "", "Re-Enter New Password");
            empty = true;
        }
        else if (!getTextViewText(R.id.newRepass).toString().trim().equals(getTextViewText(R.id.newPass).toString().trim())) {
            Utils.showDialog(NewPasswordGenration.this, "", "Password is Not Same");
            empty = true;
        }
        return empty;
    }

}
