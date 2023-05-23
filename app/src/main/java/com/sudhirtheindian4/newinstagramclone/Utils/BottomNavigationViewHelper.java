package com.sudhirtheindian4.newinstagramclone.Utils;//package com.sudhirtheindian4.newinstagramclone.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sudhirtheindian4.newinstagramclone.likes.LikesActivity;
import com.sudhirtheindian4.newinstagramclone.home.HomeActivity;
import com.sudhirtheindian4.newinstagramclone.profile.ProfileActivity;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.search.SearchActivity;
import com.sudhirtheindian4.newinstagramclone.share.ShareActivity;


//// created by user 17/08/2022
public class BottomNavigationViewHelper {
    private static final  String TAG = "BottomNavigationViewHelper";

    public  static void setUpBottomNavigationView(BottomNavigationView bottomNavigationView){
//        bottomNavigationViewEx.enableAnimation(false);
//        bottomNavigationViewEx.enableItemShiftingMode(false);
//        bottomNavigationViewEx.enableShiftingMode(false);
//        bottomNavigationViewEx.setTextVisibility(false);


    }

//public  static void setUpBottomNavigationView(BottomNavigationView bottomNavigationView){
////    bottomNavigationView.enableAnimation(false);
////    bottomNavigationView.enableItemShiftingMode(false);
////    bottomNavigationView.enableShiftingMode(false);
////    bottomNavigationView.setTextVisibility(false);
//
//
//}




    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.house:
                        Intent intent1 = new Intent(context, HomeActivity.class);  ////ACTIVITY NUM 0
                      context.startActivity(intent1);
                      callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                      break;

                    case R.id.search:
                        Intent intent2 = new Intent(context, SearchActivity.class);    ////ACTIVITY NUM 1
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                        break;

                    case R.id.circle:
                        Intent intent5 = new Intent(context, ShareActivity.class);  ////ACTIVITY NUM 2
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                        break;


                    case R.id.alert:
                        Intent intent4 = new Intent(context, LikesActivity.class);  ////ACTIVITY NUM 3
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                        break;

                    case R.id.android:
                        Intent intent3 = new Intent(context, ProfileActivity.class); ////ACTIVITY NUM 4
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                        break;






                }
                return false;
            }
        });
    }



}
