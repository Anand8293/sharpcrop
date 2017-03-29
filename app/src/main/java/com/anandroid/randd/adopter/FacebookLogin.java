package com.anandroid.randd.adopter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.anandroid.randd.Entity.FacebookUser;
import com.anandroid.randd.sharpcrop.HomeActivity;
import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.anandroid.randd.web.GetServices;
import com.anandroid.randd.web.ServiceEvents;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP 15 on 29-03-2016.
 */
public class FacebookLogin implements ServiceEvents {

   // private ProgressDialog pd;
    private Context context;
    FacebookUser facebookUser;

    public FacebookLogin(Context context, FacebookUser facebookUser){

        this.context = context;
        this.facebookUser = facebookUser;

        }

    private Map getLoginRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "facebook_login");
        params.put("token", UserPrefs.getDeviceToken(context));
        params.put("mobile", facebookUser.getMailId());
        params.put("first_name", facebookUser.getfName());
        params.put("last_name", facebookUser.getlName());
        return params;
    }

    public void doLogin() {
        Utils.LogMessage(Constants.TAG, "facebook Login Request::> " + getLoginRequest()
                + "!!");
        if (Utils.isOnline(context)) {
            GetServices bzzzLoginServices = new GetServices(FacebookLogin.this, context);
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
       /* pd = ProgressDialog.show(context, "",
                context.getString(R.string.pls_wait));
        pd.setCancelable(false);*/
    }

    @Override
    public void Finished(String methodName, Object Data) {
        /*if (pd != null)
            pd.dismiss();*/
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
       /* if (pd != null)
            pd.dismiss();*/
    }

    @Override
    public void EndedRequest() {
        // TODO Auto-generated method stub
      /*  if (pd != null)
            pd.dismiss();*/

    }
}
