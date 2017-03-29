package com.anandroid.randd.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anandroid.randd.sharpcrop.R;

/**
 * Created by Admin on 04-06-2015.
 */
public class ContentFragment extends Fragment {
    private TabLayout tabLayout;
    TextView tv;
    //This is our viewPager
    private ViewPager viewPager;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.content_fragment,container,false);
       //Adding the tabs using addTab() method

       //Creating our pager adapter
       tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
       viewPager = (ViewPager) v.findViewById(R.id.pager);
       //Add Action On Total Payment
       tv= (TextView)v.findViewById(R.id.totalAmount);
       tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getFragmentManager().beginTransaction().addToBackStack("tag")
                       .replace(R.id.frame,new AppointmentFragment()).commit();

           }
       });
       //Adding adapter to pager
       viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

       //Adding onTabSelectedListener to swipe views

       tabLayout.post(new Runnable() {
           @Override
           public void run() {
               tabLayout.setupWithViewPager(viewPager);

           }
       });

  //     tabLayout.setOnTabSelectedListener(this);
        return v;
    }


    class MyAdapter extends FragmentPagerAdapter {

        int itemsCount;
        public MyAdapter(FragmentManager fm) {
            super(fm);
            this.itemsCount = itemsCount;
        }



        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PackegesFragment.newInstance();
                case 1:
                    return ServicesFragment.newInstance();
                }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        // Banwari lal
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position)
            {
                case 0: return "Packeges";
                case 1: return "Services";
            }
            return null;
        }
    }

}
