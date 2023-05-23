package com.sudhirtheindian4.newinstagramclone.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sudhirtheindian4.newinstagramclone.LogIn.LogInActivity;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.Utils.BottomNavigationViewHelper;
import com.sudhirtheindian4.newinstagramclone.Utils.MainfeedListAdapter;
import com.sudhirtheindian4.newinstagramclone.Utils.SectionsPagerAdapter;
import com.sudhirtheindian4.newinstagramclone.Utils.UniversalImageLoader;
import com.sudhirtheindian4.newinstagramclone.Utils.ViewCommentsFragment;
import com.sudhirtheindian4.newinstagramclone.models.Photo;
import com.sudhirtheindian4.newinstagramclone.models.UserAccountSettings;

public class HomeActivity extends AppCompatActivity implements MainfeedListAdapter.OnLoadMoreItemsListener {
    @Override
    public void onLoadMoreItems() {
        Log.d(TAG, "onLoadMoreItems: displaying more photos");
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager_container + ":" + mViewPager.getCurrentItem());

        if (fragment != null) {
            fragment.displayMorePhotos();
        }

    }

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUMBER = 0;
    private static final int HOME_FRAGMENT = 1;


    /// fierbase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //// widget
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "oncreate: starting");
        mViewPager = findViewById(R.id.viewpager_container);
        mFrameLayout = findViewById(R.id.container);
        mRelativeLayout = findViewById(R.id.relLayoutParent);


        setupfirebaseAuth();
        initImageLoader();
//        Log.d(TAG,"oncreate starting");
        setUpBottomNavigationView();

        setUpViewPager();

//        mAuth.signOut();


    }

    public void onCommentThreadSelected(Photo photo, String callingActivity) {
        Log.d(TAG, "onCommentThreadSelected: selected a comment thread ");
        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putString(getString(R.string.home_activity), getString(R.string.home_activity));
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();

    }


    public void hideLayout() {
        Log.d(TAG, "hidelayout: hiding layout");
        mRelativeLayout.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);

    }


    public void showLayout() {
        Log.d(TAG, "hidelayout: showing layout");
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mFrameLayout.getVisibility() == View.VISIBLE) {
            showLayout();
        }
    }

    //// ise bar bar har activity me use  na krna pade isliye ise home yani main activity me lijh diya
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(HomeActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }



    /*
    responsible for the 3 tabs camera , message and home
     */

    private void setUpViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragmment(new CameraFragment()); /// index 0
        adapter.addFragmment(new HomeFragment()); /// index 1
        adapter.addFragmment(new MessageFragment()); /// index 2

        mViewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.instagram_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.message);
    }


    /*
    bottom navigationview set up
    */
    private void setUpBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(HomeActivity.this, this, bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);


    }



    /*
    ....................................Firebase...............................*/


    /*
    check to see if user is loged in
     */
    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkcurrent user, checking if user is logged in ");

        if (user == null) {
            Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
            startActivity(intent);
        }

    }


        /*
    set up the firebase object
     */

    private void setupfirebaseAuth() {
        Log.d(TAG, "setttng firebase auth");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                /// check current user is login
                checkCurrentUser(user);

                if (user != null) {
                    // user is signed in
                    Log.d(TAG, "onwith state changed signed in " + user.getUid());
                } else {
                    // user is signed out
                    Log.d(TAG, "onwith state changed signed out");

                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mViewPager.setCurrentItem(HOME_FRAGMENT);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) ;
        mAuth.removeAuthStateListener(mAuthListener);
    }


}