package com.anandroid.randd.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.anandroid.randd.adopter.PackageAdopter;
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
 */
public class PackegesFragment extends Fragment implements ServiceEvents {
    ProgressDialog pd;
    ListView list;
    PackageAdopter pa;
    View tv;
    ArrayList<HashMap<String,String>> packages = new ArrayList<>();
    public PackegesFragment() {
        // Required empty public constructor
    }

    public static PackegesFragment newInstance() {
        PackegesFragment fragment = new PackegesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_packeges, container, false);
        list = (ListView)v.findViewById(R.id.packeges_list);
        tv= getActivity().findViewById(R.id.totalAmount);
        getPackages();
        return v;
    }
    private void getPackages(){
        Utils.LogMessage(Constants.TAG, "Packeges Request::> " + getDoRegisterRequest()  + "!!");
        if (Utils.isOnline(getActivity())) {
            GetServices bzzzLoginServices = new GetServices(PackegesFragment.this, getActivity());
            try {
                bzzzLoginServices.RestCallAsync("", getDoRegisterRequest());
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
        params.put("tag", "packages");
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
                    packages.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("responsedata");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        HashMap<String,String> packaga = new HashMap<>();
                        packaga.put("package_name",jsonArray.getJSONObject(i).getString("package_name"));
                        packaga.put("description",jsonArray.getJSONObject(i).getString("description"));
                        packaga.put("duration",jsonArray.getJSONObject(i).getString("duration"));
                        packaga.put("image",jsonArray.getJSONObject(i).getString("image"));
                        packaga.put("price",jsonArray.getJSONObject(i).getString("price"));
                        packaga.put("services",jsonArray.getJSONObject(i).getString("services"));
                        packages.add(packaga);
                       }
                    pa = new PackageAdopter(getActivity(),packages,tv);
                    list.setAdapter(pa);
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
