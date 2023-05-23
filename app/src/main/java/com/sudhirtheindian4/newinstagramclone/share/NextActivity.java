package com.sudhirtheindian4.newinstagramclone.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.Utils.FirebaseMethods;
import com.sudhirtheindian4.newinstagramclone.Utils.UniversalImageLoader;

public class NextActivity extends AppCompatActivity {
    private static final String TAG = "NextActivity";
    private String mAppend = "file:/";
    private int imageCount = 0;
    private  String imgUrl;
    private EditText mCaption;
    private Intent intent;
    private Bitmap bitmap;







    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        mFirebaseMethods = new FirebaseMethods(NextActivity.this);
        mCaption = findViewById(R.id.caption);
        Log.d(TAG,"oncreate : got the chosen image"+getIntent().getStringExtra(getString(R.string.selected_image)));

        setupFirebaseAuth();

        ImageView backArrow = (ImageView)findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                finish();
            }

        });


        TextView share = (TextView)findViewById(R.id.tvShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");

                Toast.makeText(NextActivity.this, "attemtmpting to upload new photo", Toast.LENGTH_SHORT).show();
                String caption =mCaption.getText().toString();

                if(intent.hasExtra(getString(R.string.selected_image))){
                    imgUrl = intent.getStringExtra(getString(R.string.selected_image));
                    mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo),caption, imageCount, imgUrl,null);



                }
                else if(intent.hasExtra(getString(R.string.selected_bitmap))){
                    bitmap = (Bitmap)intent.getParcelableExtra(getString(R.string.selected_bitmap));
                    mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo),caption, imageCount, null,bitmap);


                }






                ///upload the image to firebase


            }
        });
        setImage();

    }

    private void someMethod(){
        /*
        step 1
        create a data model for photos

        step2
        add properties to the  photo objects (caption, data, imageurl,photo_id,tags, user_id)

        step3
        create the number of photo  that the user already has

        step 4
        a) upload the photo
        b) insert into photos node
        c) insert into user_photos node

         */
    }

    /*
    get the imageurl from the incoming intent and display the choosen image
     */
    private void  setImage(){
        intent = getIntent();
        ImageView image = findViewById(R.id.imageShare);
        if(intent.hasExtra(getString(R.string.selected_image))){
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            UniversalImageLoader.setImage(imgUrl,image,null,mAppend);
            Log.d(TAG,"setImage: got new image url");

        }
        else if(intent.hasExtra(getString(R.string.selected_bitmap))){
            bitmap = (Bitmap)intent.getParcelableExtra(getString(R.string.selected_bitmap));
            Log.d(TAG,"setImage: got new bitmap");
            image.setImageBitmap(bitmap);

        }


    }



      /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Log.d(TAG,"ondatachange: image count:"+imageCount);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageCount = mFirebaseMethods.getImageCount(dataSnapshot);
                Log.d(TAG,"ondatachange: image count:"+imageCount);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}







