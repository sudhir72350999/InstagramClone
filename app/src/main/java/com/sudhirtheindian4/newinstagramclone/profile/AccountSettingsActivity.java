package com.sudhirtheindian4.newinstagramclone.profile;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.Utils.BottomNavigationViewHelper;
import com.sudhirtheindian4.newinstagramclone.Utils.FirebaseMethods;
import com.sudhirtheindian4.newinstagramclone.Utils.SectionsStatePagerAdapter;
import com.sudhirtheindian4.newinstagramclone.home.HomeActivity;

import java.util.ArrayList;


/**
 * Created by User on 18/8/2022
 */


public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUMBER = 4;

    private Context mContext;

    public SectionsStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        mContext = AccountSettingsActivity.this;
        Log.d(TAG, "onCreate: started.");
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayout1);

        setupSettingsList();
        setUpBottomNavigationView();

        setupFragments();

        getIncomingIntent();

        //setup the backarrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                finish();
            }
        });
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();

        // if there is  an image url  attaacheed as an extra , then it was choosen from the gallery/ photo fragment
        Log.d(TAG, "getIncoming :New incoming imageUrl");

        if (intent.hasExtra(getString(R.string.selected_image)) || intent.hasExtra(getString(R.string.selected_bitmap))) {
            if (intent.getStringExtra(getString(R.string.return_to_fragment)).equals(getString(R.string.edit_profile_fragment))) {

                if (intent.hasExtra((getString(R.string.selected_image)))) {
                    // set the new profile picture
                    FirebaseMethods firebaseMethods = new FirebaseMethods(AccountSettingsActivity.this);
                    firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), null, 0, intent.getStringExtra(getString(R.string.selected_image)), null);
                } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                    FirebaseMethods firebaseMethods = new FirebaseMethods(AccountSettingsActivity.this);
                    firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), null, 0, null, (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap)));


                }

            }
        }
        if (intent.hasExtra(getString(R.string.calling_activity))) {
            Log.d(TAG, "getIncoming recieved incoming intent from" + getString(R.string.profile_activity));
            setViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }

    }

    private void setupFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile_fragment)); //fragment 0
        pagerAdapter.addFragment(new SignOutFragment(), getString(R.string.sign_out_fragment)); //fragment 1
    }

    public void setViewPager(int fragmentNumber) {
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: navigating to fragment #: " + fragmentNumber);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNumber);
    }

    private void setupSettingsList() {
        Log.d(TAG, "setupSettingsList: initializing 'Account Settings' list.");
        ListView listView = (ListView) findViewById(R.id.listViewAccountSetting);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile_fragment)); //fragment 0
        options.add(getString(R.string.sign_out_fragment)); //fragement 1

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to fragment#: " + position);
                setViewPager(position);
            }
        });

    }


    private void setUpBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(AccountSettingsActivity.this, this, bottomNavigationView);


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);


    }


}













