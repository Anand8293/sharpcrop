package com.anandroid.randd.fragments;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.anandroid.randd.sharpcrop.BaseFragmentActivity;
import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends BaseFragmentActivity implements View.OnClickListener {
    String dobStr,timeStr;
    TextView datepi,timepi;
    EditText name,mobile,address;
    Button submit;
    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment, container, false);
        datepi = (TextView)v.findViewById(R.id.datepick);
        timepi = (TextView)v.findViewById(R.id.timepick);
        name =(EditText)v.findViewById(R.id.name);
        address =(EditText)v.findViewById(R.id.address);
        mobile =(EditText)v.findViewById(R.id.mobile);
        submit =(Button) v.findViewById(R.id.submit);
        setTextViewText(R.id.title_text,"Appointment");
        datepi.setOnClickListener(this);
        timepi.setOnClickListener(this);
        submit.setOnClickListener(this);
        setClick(R.id.submit,v);
        setEditText(R.id.name,v,UserPrefs.getName(getActivity()));
        setEditText(R.id.mobile,v, UserPrefs.getMobile(getActivity()));
        return v;
    }
    public void getDateTime() {
        final Dialog dialogDT = new Dialog(getActivity());
        dialogDT.setContentView(R.layout.date_time_layout);
        dialogDT.setTitle("Select date of birth");
        dialogDT.show();
        Button btnSet = (Button) dialogDT.findViewById(R.id.okBtn);
        final DatePicker dp = (DatePicker) dialogDT
                .findViewById(R.id.datePicker1);
        dp.setVisibility(View.VISIBLE);
        final TimePicker tp = (TimePicker)
                dialogDT.findViewById(R.id.timePicker1);
        tp.setVisibility(View.GONE);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobStr = dp.getYear() + "-" + (dp.getMonth() + 1) + "-"
                        + dp.getDayOfMonth() + " ";

                datepi.setText(dobStr);
                dialogDT.dismiss();
            }
        });
    }

    public void getTime() {
        final Dialog dialogDT = new Dialog(getActivity());
        dialogDT.setContentView(R.layout.date_time_layout);
        dialogDT.setTitle("Select date of birth");
        dialogDT.show();
        Button btnSet = (Button) dialogDT.findViewById(R.id.okBtn);
        final TimePicker tp = (TimePicker) dialogDT.findViewById(R.id.timePicker1);
        tp.setVisibility(View.VISIBLE);
        final DatePicker dp = (DatePicker) dialogDT.findViewById(R.id.datePicker1);
        dp.setVisibility(View.GONE);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStr = String.format("%02d",tp.getCurrentHour()) + " - " + String.format("%02d",tp.getCurrentMinute() + 1) ;
                timepi.setText(timeStr);
                dialogDT.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.timepick:
                getTime();
                break;
            case R.id.datepick:
                getDateTime();
                break;
            case R.id.submit:
                if(!isEmpty()) {
                    getFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,
                            PaymentOptionFragment.newInstance()).commit();
                }
                break;

        }
    }
    private boolean isEmpty() {
        boolean empty = false;
        if (getEditTextText(R.id.name).toString().trim().length() == 0) {
            Utils.showDialog(getActivity(), "", "Enter Name");
            empty = true;
        } else if (getTextViewText(R.id.datepick).toString().trim().length() == 0) {
            Utils.showDialog(getActivity(), "", "Select Date");
            empty = true;
        } else if (getTextViewText(R.id.timepick).toString().trim().length() == 0) {
            Utils.showDialog(getActivity(), "", "Select Time");
            empty = true;
        } else if (getEditTextText(R.id.mobile).toString().trim().length() == 0) {
            Utils.showDialog(getActivity(), "", "Enter Mobile");
            empty = true;
        }
        else if (getTextViewText(R.id.address).toString().trim().length() == 0) {
            Utils.showDialog(getActivity(), "", "Enter Address");
            empty = true;
        }


        return empty;
    }

}
