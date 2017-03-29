package com.anandroid.randd.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.utils.SdkHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.anandroid.randd.utils.Constants.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMoneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMoneyFragment extends Fragment {

    TextView notes,promoCodeClick;
    EditText amt;
    RadioGroup payment;
    Button next;
    Dialog dialog;
    ArrayList<HashMap<String,String>> allData ;
    String promo = "";

    public AddMoneyFragment() {
        // Required empty public constructor
    }

    public static AddMoneyFragment newInstance() {
        AddMoneyFragment fragment = new AddMoneyFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_money, container, false);
        notes = (TextView)v.findViewById(R.id.notes);
        promoCodeClick = (TextView)v.findViewById(R.id.promo_code_click);
        promoCodeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPromoCode();
            }
        });
        payment = (RadioGroup)v.findViewById(R.id.payment_option);
        amt = (EditText) v.findViewById(R.id.amount);
        next = (Button) v.findViewById(R.id.submit);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //               int selectedId = payment.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                switch (payment.getCheckedRadioButtonId())
                {
                    case R.id.paytm :
                        break;
                    case R.id.payumoney:
                        makePaymentPU();
                    default:
                        break;
                }

//               Toast.makeText(getActivity(),""+rb.getText(),Toast.LENGTH_LONG).show();

                //
            }
        });
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want Change/Remove Promo Code ??")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notes.setVisibility(View.INVISIBLE);
                                        promoCodeClick.setVisibility(View.VISIBLE);
                                        dialog.dismiss();
                                    }

                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setCancelable(false).create().show();
            }
        });
        getPromoList();
        return v;
    }

    private void showPromoCode() {
        // custom dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.show_hide_item, null);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        dialog.show();
        // set the custom dialog components - text, image and button
        ListView promoList = (ListView) dialog.findViewById(R.id.sub_list_item);
        PromoAdopter pa=new PromoAdopter(getActivity(),allData);
        promoList.setAdapter(pa);

    }
    public class PromoAdopter extends BaseAdapter
    {
        TextView procode,promoText;
        Button apply;
        Context context;
        ArrayList<HashMap<String,String>> data;
        public PromoAdopter(Context context,ArrayList<HashMap<String,String>> data)
        {
            this.context = context;
            this.data = data;

        }
        @Override
        public int getCount() {
            return data.size();
        }


        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.promo_code_list_item, null);

            procode = (TextView)convertView.findViewById(R.id.promo_code);
            procode.setText(data.get(position).get("prmocode_name"));
            promoText = (TextView)convertView.findViewById(R.id.promo_text);
            promoText.setText("select "+data.get(position).get("prmocode_name")+" And get "+data.get(position).get("rate")+
                    " cash Back in your Wallet \n It is valide till "+data.get(position).get("prmocode_name"));
            apply = (Button) convertView.findViewById(R.id.apply);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                    promoCodeClick.setVisibility(View.INVISIBLE);
                    notes.setVisibility(View.VISIBLE);
                    notes.setText("Promo Code "+data.get(position).get("prmocode_name")+" is Selected "+data.get(position).get("rate")+
                            "\n cash Back will add in your Wallet in 24 hour");
                    promo=data.get(position).get("prmocode_name");
                    }
            });
            return convertView;
        }
    }


    public ArrayList<HashMap<String,String>> getPromoList()
    {
        allData = new ArrayList<>();
        StringRequest sr = new StringRequest(Request.Method.POST,"http://sharpcrop.com/app/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        Log.e(Constants.TAG, "Country List Response::> " + res);
                        if (res.trim().length() > 0) {

                            Log.e(Constants.TAG, "serviceState==1");
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                String msg = jsonObject1.getString("success");
                                if (msg.equalsIgnoreCase("1"))
                                {
                                    allData.clear();
                                    JSONArray jsonArray = jsonObject.getJSONArray("responsedata");
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        HashMap<String,String> singleData = new HashMap<>();
                                        singleData.put("prmocode_name",jsonArray.getJSONObject(i).getString("prmocode_name"));
                                        singleData.put("rate",jsonArray.getJSONObject(i).getString("rate"));
                                        singleData.put("start_date",jsonArray.getJSONObject(i).getString("start_date"));
                                        singleData.put("end_date",jsonArray.getJSONObject(i).getString("end_date"));
                                        allData.add(singleData);
                                    }
                                }
                                else
                                    Toast.makeText(getActivity(),jsonObject1.getString("msg"),Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        else {
                            Utils.showDialog(getActivity(), "", "Response not getting from server");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialogMessage(error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("tag", "prmocode");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        sr.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);
        Log.d(Constants.TAG,"Data Array =>"+allData.toString());
        return allData;
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private double getAmount() {


        Double amount = 0.0;

        if (isDouble(amt.getText().toString())) {
            amount = Double.parseDouble(amt.getText().toString());
            return amount;
        } else {
            Toast.makeText(getActivity(), "Paying Default Amount ₹10", Toast.LENGTH_LONG).show();
            return amount;
        }
    }

    public void makePaymentPU() {

        String phone = "9024067948";
        String productName = "Sharpcrop";
        String firstName = "david";
        String txnId = "0nf7" + System.currentTimeMillis();
        String email = "daviddeevan91@gmail.com";
        String sUrl = "https://www.PayUmoney.com/mobileapp/PayUmoney/success.php";
        String fUrl = "https://www.PayUmoney.com/mobileapp/PayUmoney/failure.php";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = false;
        String key = "hF0cVi";
        String merchantId = "5049334";

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();


        builder.setAmount(getAmount())
                .setTnxId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setIsDebug(isDebug)
                .setKey(key)
                .setMerchantId(merchantId);

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

//             server side call required to calculate hash with the help of <salt>
//             <salt> is already shared along with merchant <key>
     /*        serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
*/

        // Recommended
        calculateServerSideHashAndInitiatePayment(paymentParam);

//        testing purpose

        String salt = "zxIWo0dI";
        String serverCalculatedHash = hashCal(key + "|" + txnId + "|" + getAmount() + "|" + productName + "|"
                + firstName + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "|" + salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);

    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(getActivity(), "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString(SdkConstants.RESULT), Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            if (data != null && data.hasExtra("result")) {
                String responsePayUmoney = data.getStringExtra("result");
                if (SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }


            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                showDialogMessage("Payment Success Id : " + paymentId);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        }
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
}