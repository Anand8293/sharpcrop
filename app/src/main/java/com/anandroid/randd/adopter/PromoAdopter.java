package com.anandroid.randd.adopter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anandroid.randd.sharpcrop.R;

import java.util.HashMap;

/**
 * Created by Anand on 07-Feb-17.
 */
public class PromoAdopter extends BaseAdapter
{
    TextView procode,promoText;
    Button apply;
    Context context;
    HashMap<String,String> data;
    public PromoAdopter(Context context,HashMap<String,String> data)
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.promo_code_list_item, null);
        }
        procode = (TextView)convertView.findViewById(R.id.promo_code);
        promoText = (TextView)convertView.findViewById(R.id.promo_code_click);
        apply = (Button) convertView.findViewById(R.id.apply);
        return convertView;
    }
}
