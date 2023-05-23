package com.sudhirtheindian4.newinstagramclone.likes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.Utils.BottomNavigationViewHelper;
import com.sudhirtheindian4.newinstagramclone.home.HomeActivity;

public class LikesActivity extends AppCompatActivity {
    private  static final int ACTIVITY_NUMBER =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpBottomNavigationView();
    }
    private void setUpBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.enableNavigation(LikesActivity.this,bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(LikesActivity.this,this,bottomNavigationView);

        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

}