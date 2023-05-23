package com.sudhirtheindian4.newinstagramclone.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.Utils.ViewCommentsFragment;
import com.sudhirtheindian4.newinstagramclone.Utils.ViewPostFragment;
import com.sudhirtheindian4.newinstagramclone.Utils.ViewProfileFragment;
import com.sudhirtheindian4.newinstagramclone.models.Photo;
import com.sudhirtheindian4.newinstagramclone.models.User;

public class ProfileActivity extends AppCompatActivity implements
        ProfileFragment.OnGridImageSelectedListener,
        ViewPostFragment.OnCommentThreadSelectedListener,
        ViewProfileFragment.OnGridImageSelectedListener {

    private  static final String tag ="ProfileActivity";

    @Override
    public void onCommentThreadSelectedListener(Photo photo) {
        Log.d(tag,"onCommentThreadSelectedListener: selected a comment thread");
        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo),photo);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();




    }


    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
  Log.d(tag,"onGridItemSelected : selected an image gridview:"+photo.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo),photo);
        args.putInt(getString(R.string.activity_number),activityNumber);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();


    }


    private  static final int ACTIVITY_NUMBER =4;
    private  static  final  int NUM_GRID_COLUMNS=3;
    private Context mContext = ProfileActivity.this;


    private ProgressBar progressBar;

    private  ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//
//        progressBar = findViewById(R.id.profileProgressbar);
//        progressBar.setVisibility(View.GONE);
        init();

//        setUpBottomNavigationView();
//        setupToolbar();
//        setUpActivityWidget();
//        setProfileImage();
//        tempGridSetUp();



    }

    private void init(){
        Log.d(tag,"init inflating "+getString(R.string.profile_fragment));

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(tag,"init: searching for user object attached as intent extra");
            if(intent.hasExtra(getString(R.string.intent_user))){
                User user = intent.getParcelableExtra(getString(R.string.intent_user));
                if(!user.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                          Log.d(tag,"init: inflating view profile");

                        ViewProfileFragment fragment = new ViewProfileFragment();
                        Bundle args = new Bundle();
                        args.putParcelable(getString(R.string.intent_user),intent.getParcelableExtra(getString(R.string.intent_user)));
                        fragment.setArguments(args);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container,fragment);
                        transaction.addToBackStack(getString(R.string.view_profile_fragment));
                        transaction.commit();

                    }
                      else {
                        Log.d(tag,"init: inflating profile");

                        ProfileFragment fragment = new ProfileFragment();
                        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container,fragment);
                        transaction.addToBackStack(getString(R.string.profile_fragment));
                        transaction.commit();
                    }
                }
                else {
                    Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
            else {
                Log.d(tag,"init: inflating profile");

                ProfileFragment fragment = new ProfileFragment();
                FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,fragment);
                transaction.addToBackStack(getString(R.string.profile_fragment));
                transaction.commit();
            }

        }


    }




//
//    private  void tempGridSetUp(){
//        ArrayList<String> imgURLs = new ArrayList<>();
//
//        imgURLs.add("https://pbs.twimg.com/profile_images/616076655547682816/6gMRtQyY.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
//        imgURLs.add("http://i.imgur.com/EwZRpvQ.jpg");
//        imgURLs.add("http://i.imgur.com/JTb2pXP.jpg");
//        imgURLs.add("https://i.redd.it/59kjlxxf720z.jpg");
//        imgURLs.add("https://i.redd.it/pwduhknig00z.jpg");
//        imgURLs.add("https://i.redd.it/clusqsm4oxzy.jpg");
//        imgURLs.add("https://i.redd.it/svqvn7xs420z.jpg");
//        imgURLs.add("http://i.imgur.com/j4AfH6P.jpg");
//        imgURLs.add("https://i.redd.it/89cjkojkl10z.jpg");
//        imgURLs.add("https://i.redd.it/aw7pv8jq4zzy.jpg");
//
//
//
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2022/07/25/18/47/waterfall-7344396_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2022/07/25/18/47/waterfall-7344396_960_720.jpg");
//        imgURLs.add("https://cdn.pixabay.com/photo/2015/10/30/20/13/sunrise-1014712_960_720.jpg");
//        imgURLs.add("https://i.redd.it/59kjlxxf720z.jpg");
//        imgURLs.add("https://i.redd.it/pwduhknig00z.jpg");
//        imgURLs.add("https://i.redd.it/clusqsm4oxzy.jpg");
//        imgURLs.add("https://i.redd.it/svqvn7xs420z.jpg");
//        imgURLs.add("https://i.imgur.com/j4AfH6P.jpg");
//        imgURLs.add("https://i.redd.it/89cjkojkl10z.jpg");
//        imgURLs.add("https://i.redd.it/aw7pv8jq4zzy.jpg");
//
//        setUpImageGrid(imgURLs);
//
//    }
//    private void setUpImageGrid(ArrayList<String> imgUrls){
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//        gridView.setColumnWidth(imageWidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this,R.layout.layout_grid_imageview,"",imgUrls);
//        gridView.setAdapter(adapter);
//
//    }
//
//    private  void setProfileImage(){
//        //// setting profile photo
//        ////dono use https:// in image url because this is appended already
//        String imgUrl = "cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg";
//        UniversalImageLoader.setImage(imgUrl,profilePhoto,progressBar,"https://");
//
//    }
//    private  void setUpActivityWidget(){
//        progressBar = findViewById(R.id.profileProgressbar);
//        progressBar.setVisibility(View.GONE);
//        profilePhoto = findViewById(R.id.profile_photo);
//
//
//    }
//
//    private void setupToolbar(){
//        Toolbar toolbar = findViewById(R.id.profileToolbar);
//        setSupportActionBar(toolbar);
//        ImageView profileMenu = findViewById(R.id.profileMenu);
//        profileMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*
//                cliked  navigate the account setting
//                 */
//                Intent intent = new Intent(ProfileActivity.this,AccountSettingsActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//    }
//
//
//
//    private void setUpBottomNavigationView() {
//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.enableNavigation(ProfileActivity.this,bottomNavigationView);
//        Menu menu = bottomNavigationView.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
//        menuItem.setChecked(true);
//    }
//}