package com.anandroid.randd.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.anandroid.randd.adopter.PackageAdopter;
import com.anandroid.randd.adopter.ServicesAdopter;
import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.utils.Constants;
import com.anandroid.randd.utils.Utils;
import com.anandroid.randd.web.GetServices;
import com.anandroid.randd.web.ServiceEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesFragment extends Fragment implements ServiceEvents {

    ProgressDialog pd;
    ListView list;
    ServicesAdopter sa;
    View tv;
    ArrayList<HashMap<String,String>> services = new ArrayList<>();
    public ServicesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_services, container, false);
        list = (ListView)v.findViewById(R.id.services_list);
        tv= getActivity().findViewById(R.id.totalAmount);
        getServices();
        return v;
    }
    private void getServices(){
        Utils.LogMessage(Constants.TAG, "Services Request::> " + getDoRegisterRequest()  + "!!");
        if (Utils.isOnline(getActivity())) {
            GetServices bzzzLoginServices = new GetServices(ServicesFragment.this, getActivity());
            try {
                bzzzLoginServices.RestCallAsync("services.php", getDoRegisterRequest());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            Utils.showDialog(getActivity(), "",
                    getString(R.string.no_network));
    }
    private Map getDoRegisterRequest() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("tag", "services");
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
                    JSONArray jsonArray = jsonObject.getJSONArray("responsedata");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("main_category");
                        for (int j=0;j<jsonArray1.length();j++)
                        {
                            services.clear();
                            JSONArray jsonArray3 = jsonArray1.getJSONObject(j).getJSONArray("service");
                            for (int k=0;k<jsonArray3.length();k++)
                            {
                                HashMap<String, String> service = new HashMap<>();
                                service.put("main_category", jsonArray3.getJSONObject(k).getString("main_category"));
                                service.put("description", jsonArray3.getJSONObject(k).getString("description"));
                                service.put("duration", jsonArray3.getJSONObject(k).getString("duration"));
                                service.put("image", jsonArray3.getJSONObject(k).getString("image"));
                                service.put("price", jsonArray3.getJSONObject(k).getString("price"));
                                service.put("service", jsonArray3.getJSONObject(k).getString("service"));
                                services.add(service);
                            }

                        }

                    }
                    sa = new ServicesAdopter(getActivity(),services,tv);
                    list.setAdapter(sa);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Utils.showDialog(getActivity(), "",
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

