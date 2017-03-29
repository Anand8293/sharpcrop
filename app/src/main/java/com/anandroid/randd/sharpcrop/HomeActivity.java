package com.anandroid.randd.sharpcrop;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anandroid.randd.fragments.AboutUsFragment;
import com.anandroid.randd.fragments.AddMoneyFragment;
import com.anandroid.randd.fragments.ContactUsFragment;
import com.anandroid.randd.fragments.ContentFragment;
import com.anandroid.randd.fragments.PackegesFragment;
import com.anandroid.randd.fragments.RateAndReview;
import com.anandroid.randd.fragments.RefereFriendFragment;
import com.anandroid.randd.fragments.ServicesFragment;
import com.anandroid.randd.utils.CircularNetworkImageView;
import com.anandroid.randd.utils.UserPrefs;
import com.anandroid.randd.utils.Utils;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;
/**
 * Created by Anand on 4/02/17.
 */
public class HomeActivity extends BaseActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    View headerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setTextViewText(R.id.title_text, "Home");
        headerLayout =   navigationView.getHeaderView(0);

        // Edit profile Action

        headerLayout.findViewById(R.id.edit_profile)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this,EditProfileActivity.class));
                    }
                });

        ContentFragment fragment = new ContentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked())
                     menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
//                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        ContentFragment fragment = new ContentFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.services:
//                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,new ServicesFragment()).commit();
                        return true;

                    case R.id.packeges:
//                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,new PackegesFragment()).commit();
                        return true;

                    case R.id.payment:
//                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        return true;
                    case R.id.offer:
//                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        return true;
                    case R.id.about:
//                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,new AboutUsFragment()).commit();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        return true;
                    case R.id.notification:
 //                       Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        return true;
                    case R.id.contact:
 //                       Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,new ContactUsFragment()).commit();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        return true;
                    case R.id.review:
//                       Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,new RateAndReview()).commit();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        return true;
                    case R.id.refered:
//                       Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,new RefereFriendFragment()).commit();
                        setTextViewText(R.id.title_text,menuItem.getTitle().toString());
                        return true;
                    case R.id.logout:
                        Utils.showDialog(HomeActivity.this, "", "Succefully Logout");
                        UserPrefs.resetPrefs(HomeActivity.this);
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_addtocard) {
            getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.frame,new AddMoneyFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView)headerLayout.findViewById(R.id.username)).setText(UserPrefs.getName(this));
        ((TextView)headerLayout.findViewById(R.id.useremail)).setText( UserPrefs.getUserUniqueId(this));
        CircleImageView profile_Image = (CircleImageView)headerLayout.findViewById(R.id.profile_image);

        //Set profile Image if Available

        if (UserPrefs.getProfileImage(this) != null && !UserPrefs.getProfileImage(this).equals(""))
        {
            Glide.with(this).load(UserPrefs.getProfileImage(this))
                    .thumbnail(0.5f)
                    .override(200, 200)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_Image);
        }

    }
}
