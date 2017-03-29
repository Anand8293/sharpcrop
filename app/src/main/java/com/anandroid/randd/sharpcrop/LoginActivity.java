package com.anandroid.randd.sharpcrop;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anandroid.randd.Entity.GoogleUser;
import com.anandroid.randd.adopter.GoogleLogin;
import com.anandroid.randd.service.MyFirebaseInstanceIDService;
import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.anandroid.randd.web.GetServices;
import com.anandroid.randd.web.ServiceEvents;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements ServiceEvents ,GoogleApiClient.OnConnectionFailedListener
 {
    private static final String TAG = "RegIntentService";
    public static final String REGISTRATION_COMPLETE = "RegistrationComplite";
    private static final String[] TOPICS = {"global"};
    private static final String REGISTRATION_ERROR = "RegistrationError";
    private ProgressDialog pd;
    private LoginButton fb_login_Button;
    private CallbackManager callbackManager;

    //Signin button
   private SignInButton google_login_Button;

    //Signing Options
   private GoogleSignInOptions gso;

    //google api client
   private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_page);
        callbackManager = CallbackManager.Factory.create();
        Log.e("Token ID ", UserPrefs.getDeviceToken(LoginActivity.this));
        //      setClick(R.id.btn_signin);
             setClick(R.id.signUp);
             setClick(R.id.facebookLogin);
             setClick(R.id.forgotPass);
             setClick(R.id.login);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        google_login_Button = (SignInButton) findViewById(R.id.login_button);
        google_login_Button.setSize(SignInButton.SIZE_WIDE);
        google_login_Button.setScopes(gso.getScopeArray());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setClick(R.id.login_button);
        Log.v("Token", "Token Is " + UserPrefs.getDeviceToken(this));

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(LoginActivity.this, "End With "+intent.getAction().toString(), Toast.LENGTH_LONG).show();
                if (intent.getAction().endsWith(REGISTRATION_COMPLETE)) {
                    Log.v("Token", "Token Is " +intent.getStringExtra("token"));
                    UserPrefs.setDeviceToken(intent.getStringExtra("token"),LoginActivity.this);
                }
            }
        };
        connectionCheck();
    }



     @Override
     public void onBackPressed() {
         super.onBackPressed();
         finish();
     }

     @Override
     public void onClick(View v) {
         switch (v.getId()) {

             case R.id.login:
                 if(!isEmpty()){
                     doLogin();
                 }
                 break;
             case R.id.facebookLogin:
                    if(!getButtonText(R.id.facebookLogin).equalsIgnoreCase("Log Out"))
                        startActivity(new Intent(LoginActivity.this, FaceboolLoginActivity.class));
                 break;
             case R.id.signUp:
                 startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                 break;
             default:
                 break;
             case R.id.login_button:
                 signIn();
                 break;
             case R.id.forgotPass:
                 startActivity(new Intent(LoginActivity.this,ForgatPassword.class));
                 break;

         }
     }


    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(Constants.TAG, "Name::> " + acct.getDisplayName());
            Log.e(Constants.TAG, "Email::> " + acct.getEmail());
            Log.e(Constants.TAG, "Token Id::> " + acct.getId());
            GoogleUser googleUser = new GoogleUser();
            googleUser.setDeviceId(UserPrefs.getDeviceToken(LoginActivity.this));
            googleUser.setGoogleUserId(acct.getId());
            googleUser.setMailId(acct.getEmail());
            googleUser.setName(acct.getDisplayName());
            UserPrefs.setUserUniqueId(acct.getEmail(), this);
            UserPrefs.setName(acct.getDisplayName(),this);
            UserPrefs.setProfileImage(String.valueOf(acct.getPhotoUrl()),this);
            GoogleLogin googleLogin = new GoogleLogin(LoginActivity.this, googleUser);
            googleLogin.doLogin();
        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


     private void doLogin(){
         Utils.LogMessage(Constants.TAG, "User Login Request::> " + getDoLoginRequest()
                 + "!!");
         if (Utils.isOnline(this)) {
             GetServices bzzzLoginServices = new GetServices(LoginActivity.this, this);
             try {
                 bzzzLoginServices.RestCallAsync("", getDoLoginRequest());
             } catch (Exception e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
         } else
             Utils.showDialog(LoginActivity.this, "",
                     getString(R.string.no_network));
     }
     private Map getDoLoginRequest() {
         Map<String,String> params = new HashMap<String, String>();
         params.put("tag", "login");
         params.put("email", getTextViewText(R.id.email).toString().trim());
         params.put("password", getTextViewText(R.id.password).toString().trim());
         params.put("token", UserPrefs.getDeviceToken(this));
         return params;
     }


     @Override
     public void StartedRequest() {
         pd = ProgressDialog.show(LoginActivity.this, "",
                 getString(R.string.pls_wait));
         pd.setCancelable(false);
     }

     @Override
     public void Finished(String methodName, Object Data) {
         if (pd != null)
             pd.dismiss();
         String res = (String) Data;

         Log.e(Constants.TAG, "List Response::> " + res);
         if (res.trim().length() > 0) {
             try {
                 JSONObject jsonObject = new JSONObject(res);
                 JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                 String msg = jsonObject1.getString("success");
                 if (msg.equalsIgnoreCase("1")) {
                     UserPrefs.setUserUniqueId(getTextViewText(R.id.email).toString().trim(),this);
                     UserPrefs.setName(jsonObject.getJSONObject("responsedata").getString("fname")+" "
                             +jsonObject.getJSONObject("responsedata").getString("lname"),this);
                     startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                     finish();
                 }
                 else
                     Toast.makeText(this,jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();

             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }

         else {
             Utils.showDialog(LoginActivity.this, "", "Response not getting from server");
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
             Utils.showDialog(LoginActivity.this, "", "Enter email");
             empty = true;
         }   else if (getTextViewText(R.id.password).toString().trim().length() == 0) {
             Utils.showDialog(LoginActivity.this, "", "Enter Password");
             empty = true;
         }
         return empty;
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         if (requestCode == RC_SIGN_IN) {
             GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
             handleSignInResult(result);
         } else
             callbackManager.onActivityResult(requestCode, resultCode, data);
     }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public void connectionCheck() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
    //            Toast.makeText(LoginActivity.this, "Google Service is Not available Right Now Please Install It", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, LoginActivity.this);
            } else {
   //             Toast.makeText(LoginActivity.this, "Google Service is Not Supported Your Device", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, LoginActivity.this);
            }

        } else {
  //          Toast.makeText(LoginActivity.this, "Google Service is Supported Your Device", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MyFirebaseInstanceIDService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager .getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

    }
 }
