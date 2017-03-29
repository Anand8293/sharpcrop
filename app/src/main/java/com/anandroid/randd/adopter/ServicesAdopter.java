package com.anandroid.randd.adopter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anandroid.randd.sharpcrop.R;
import com.anandroid.randd.utils.UserPrefs;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anand on 03-Feb-17.
 */

public class ServicesAdopter extends BaseAdapter {
    Activity context;
    ArrayList<HashMap<String,String>> data;
    TextView tv;
    public ServicesAdopter(Activity context, ArrayList<HashMap<String,String>> data, View tv)
    {
      this.context = context;
      this.data = data;
      this.tv = (TextView)tv;
      }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final int[] personCount = {1};
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.packages_and__services_list_item , null);
         //Generate list View from ArrayList
        ImageView packegeImage = (ImageView)view.findViewById(R.id.menu_image);
        final ListView itemList = (ListView)view.findViewById(R.id.packeges_item);
        final LinearLayout relativeLayout = (LinearLayout)view.findViewById(R.id.sub_view);
        Glide.with(context).load(data.get(position).get("image"))
                .thumbnail(0.5f)
                .override(200, 200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(packegeImage);
        relativeLayout.setVisibility(View.GONE);
        TextView packegeTitle = (TextView)view.findViewById(R.id.menu_title);
        TextView plus = (TextView)view.findViewById(R.id.plus);
        final TextView persontext = (TextView)view.findViewById(R.id.total_person);
        TextView minus = (TextView)view.findViewById(R.id.minus);
        packegeTitle.setText(data.get(position).get("main_category"));
        //Array list of countries
        ArrayList<Country> countryList = new ArrayList<Country>();
        for (int i=0;i<data.size();i++) {
            Country country = new Country(Integer.parseInt(data.get(position).get("price")),
                    data.get(position).get("service"), false);
            countryList.add(country);
        }

        //create an ArrayAdaptar from the String Array
        final MyCustomAdapter dataAdapter = new MyCustomAdapter(context,R.layout.services_list_item, countryList);
        // Assign adapter to ListView
        itemList.setAdapter(dataAdapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // When clicked, show a toast with the TextView text
                Country country = (Country) parent.getItemAtPosition(position);
 //               Toast.makeText(context,"Clicked on Row: " + country.getName(),Toast.LENGTH_LONG).show();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                personCount[0]++;
                if(personCount[0]>1) {
                    ArrayList<Country> countryList = dataAdapter.countryList;
                    for(int i=0;i<countryList.size();i++){
                        Country country = countryList.get(i);
                        if(country.isSelected()){
                            UserPrefs.totalAmount+=country.getCode();
                            responseText.append("\n" + country.getName());
                        }
                    }
 //                   Toast.makeText(context,responseText, Toast.LENGTH_LONG).show();
                    tv.setText("Total Process ("+UserPrefs.totalAmount+")");
                    persontext.setText(personCount[0] + " Person");
                }
                else persontext.setText("Person");

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                if(personCount[0]>1) {
                    ArrayList<Country> countryList = dataAdapter.countryList;
                    for(int i=0;i<countryList.size();i++){
                        Country country = countryList.get(i);
                        if(country.isSelected()){
                            UserPrefs.totalAmount-=country.getCode();
                            responseText.append("\n" + country.getName());
                        }
                    }
 //                   Toast.makeText(context,responseText, Toast.LENGTH_LONG).show();
                    tv.setText("Total Process ("+UserPrefs.totalAmount+")");
                    persontext.setText(personCount[0] + " Person");
                    personCount[0]--;
                }
                else
                    persontext.setText("Person");

            }
        });

        //setVisiblity to List Item
        packegeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relativeLayout.getVisibility()==View.GONE)
                    relativeLayout.setVisibility(View.VISIBLE);
                else
                    relativeLayout.setVisibility(View.GONE);
            }
        });
       return view;
    }

    public class Country {

        int price = 0;
        String name = null;
        boolean selected = false;

        public Country(int price, String name, boolean selected) {
            super();
            this.price = price;
            this.name = name;
            this.selected = selected;
        }

        public int getCode() {
            return price;
        }
        public void setCode(int price) {
            this.price = price;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isSelected() {
            return selected;
        }
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

    }


    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = countryList;
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.services_list_item, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.price);
                holder.name = (CheckBox) convertView.findViewById(R.id.service);
                convertView.setTag(holder);
                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Country country = (Country) cb.getTag();
                        if(cb.isChecked()) {
                            UserPrefs.totalAmount+=country.getCode();
                            tv.setText("Total Process ("+UserPrefs.totalAmount+")");
                        }
                        else{
                            UserPrefs.totalAmount-=country.getCode();
                            tv.setText("Total Process ("+UserPrefs.totalAmount+")");
                            }
//                        Toast.makeText(context, "Clicked on Checkbox: " + cb.getText() + " is Total Amount = " +UserPrefs.totalAmount, Toast.LENGTH_LONG).show();
                        country.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.code.setText(" (" +  country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);
            return convertView;

        }

    }

}

