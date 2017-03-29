package com.anandroid.randd.adopter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.anandroid.randd.Entity.GoogleUser;
import com.anandroid.randd.Entity.User;
import com.anandroid.randd.sharpcrop.HomeActivity;
import com.anandroid.randd.sharpcrop.R;
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

/**
 * Created by HP 15 on 29-03-2016.
 */
public class GoogleLogin implements ServiceEvents {

    private ProgressDialog pd;
    private Context context;
    GoogleUser googleUser;

    public GoogleLogin(Context context, GoogleUser googleUser){
        this.context = context;
        this.googleUser = googleUser;
        }

    private Map getLoginRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "google_login");
        params.put("email", googleUser.getMailId());
        params.put("first_name", "");
        params.put("last_name", "");
        params.put("token",UserPrefs.getDeviceToken(context));
        return params;
    }

    public void doLogin() {
        Utils.LogMessage(Constants.TAG, "Google Login Request::> " + getLoginRequest()
                + "!!");
        if (Utils.isOnline(context)) {
            GetServices bzzzLoginServices = new GetServices(GoogleLogin.this, context);
            try {
                bzzzLoginServices.RestCallAsync("", getLoginRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(context, "",
                    context.getString(R.string.no_network));
    }


    @Override
    public void StartedRequest() {
        pd = ProgressDialog.show(context, "", context.getString(R.string.pls_wait));
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
                if (msg.equalsIgnoreCase("1")) {
                   context.startActivity(new Intent(context, HomeActivity.class));
                   return;
                    }
                else
                    Toast.makeText(context, jsonObject1.getString("msg"), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.showDialog(context, "",
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
