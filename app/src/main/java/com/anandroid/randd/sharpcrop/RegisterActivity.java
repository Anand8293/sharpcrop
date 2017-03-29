package com.anandroid.randd.sharpcrop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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

public class RegisterActivity extends BaseActivity implements ServiceEvents {

    private ProgressDialog pd;
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        cb=(CheckBox)findViewById(R.id.checkbox);
        setClick(R.id.register);
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

          case R.id.register:
                if(!isEmpty()){
                 doRegister();
                }
                break;
            case R.id.signIn :
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            default:
                break;

        }
    }

    private void doRegister(){
        Utils.LogMessage(Constants.TAG, "User Registration Request::> " + getDoRegisterRequest()
                + "!!");
        if (Utils.isOnline(this)) {
            GetServices bzzzLoginServices = new GetServices(RegisterActivity.this, this);
            try {
                bzzzLoginServices.RestCallAsync("", getDoRegisterRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(RegisterActivity.this, "",
                    getString(R.string.no_network));
    }
    private Map getDoRegisterRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "register");
        params.put("email", getTextViewText(R.id.email).toString().trim());
        params.put("fname", getTextViewText(R.id.fname).toString().trim());
        params.put("lname", getTextViewText(R.id.lname).toString().trim());
        params.put("mobile", getTextViewText(R.id.mobile).toString().trim());
        params.put("password", getTextViewText(R.id.password).toString().trim());
        params.put("token", UserPrefs.getDeviceToken(this));
        return params;
    }


    @Override
    public void StartedRequest() {
        pd = ProgressDialog.show(RegisterActivity.this, "",
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
                        JSONObject jsonObject2 = jsonObject.getJSONObject("responsedata");
                        Toast.makeText(getApplicationContext(),jsonObject2.getString("otp"),Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, OTPActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

         else {
            Utils.showDialog(RegisterActivity.this, "",
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
            Utils.showDialog(RegisterActivity.this, "", "Enter email");
            empty = true;
        }  else if (getTextViewText(R.id.fname).toString().trim().length() == 0) {
            Utils.showDialog(RegisterActivity.this, "", "Enter First Name");
            empty = true;
        }
        else if (getTextViewText(R.id.lname).toString().trim().length() == 0) {
            Utils.showDialog(RegisterActivity.this, "", "Enter Last Name");
            empty = true;
        }
        else if (!Utils.isValidEmail(getTextViewText(R.id.email)
                .toString().trim())) {
            Utils.showDialog(RegisterActivity.this, "", "Enter valid email");
            empty = true;
        } else if (getTextViewText(R.id.mobile).toString().trim().length() == 0) {
            Utils.showDialog(RegisterActivity.this, "", "Enter Mobile");
            empty = true;
        }  else if (getTextViewText(R.id.password).toString().trim().length() == 0) {
            Utils.showDialog(RegisterActivity.this, "", "Enter Password");
            empty = true;
        } else if (!cb.isChecked()) {
            Utils.showDialog(RegisterActivity.this, "", "Please Agree with Term & Condition");
            empty = true;
        }
        return empty;
    }


}
