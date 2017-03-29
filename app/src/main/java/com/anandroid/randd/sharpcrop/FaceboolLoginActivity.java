package com.anandroid.randd.sharpcrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anandroid.randd.Entity.FacebookUser;
import com.anandroid.randd.adopter.FacebookLogin;
import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FaceboolLoginActivity extends AppCompatActivity {
    private  CallbackManager callbackManager;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e(Constants.TAG, loginResult.getAccessToken().getUserId());
                        // FacebookLogin facebookLogin = new FacebookLogin(LoginActivity.this, loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        spinner.setVisibility(View.GONE);
                                       try {
                                            Log.e(Constants.TAG, object.toString());
                                            FacebookUser facebookUser = new FacebookUser();
                                            facebookUser.setFbUserId(object.getString("id"));
                                            facebookUser.setlName(object.getString("first_name"));
                                            facebookUser.setfName(object.getString("last_name"));
                                            facebookUser.setDeviceId(UserPrefs.getDeviceToken(FaceboolLoginActivity.this));
                                            facebookUser.setDob("");
                                            facebookUser.setGender(object.getString("gender"));
                                            facebookUser.setMailId(object.getString("email"));
                                            UserPrefs.setUserUniqueId(object.getString("email"),FaceboolLoginActivity.this);
                                            UserPrefs.setName(object.getString("first_name")+" "+object.getString("last_name"),FaceboolLoginActivity.this);
                                            FacebookLogin facebookLogin = new FacebookLogin(FaceboolLoginActivity.this,facebookUser);
                                            facebookLogin.doLogin();

                                            //startActivity(new Intent(FaceboolLoginActivity.this, HomeActivity));
                                        }
                                        catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email, gender, location");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        finish();
                        Toast.makeText(FaceboolLoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        finish();
                        Toast.makeText(FaceboolLoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==0)
            finish();
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


}
