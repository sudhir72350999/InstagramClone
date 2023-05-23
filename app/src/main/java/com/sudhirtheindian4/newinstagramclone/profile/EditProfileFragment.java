package com.sudhirtheindian4.newinstagramclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.Utils.FirebaseMethods;
import com.sudhirtheindian4.newinstagramclone.Utils.UniversalImageLoader;
import com.sudhirtheindian4.newinstagramclone.dialog.ConfirmPasswordDialog;
import com.sudhirtheindian4.newinstagramclone.models.User;
import com.sudhirtheindian4.newinstagramclone.models.UserAccountSettings;
import com.sudhirtheindian4.newinstagramclone.models.UserSettings;
import com.sudhirtheindian4.newinstagramclone.share.ShareActivity;

import java.security.Provider;
import java.util.Queue;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener {

    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onconfirmpassword: got the password" + password);


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
//                .getCredential("user@example.com", "password1234");
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

// Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");

                            ///********* this fetchProvidersForEmail is old , it will not work now
//                            mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
//
//                                }
//                            })

                            /// check to use if  the email is not already present in the database
                            mAuth.fetchSignInMethodsForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.isSuccessful()) {

                                        try {
                                            if (task.getResult().getSignInMethods().size() == 1) {
                                                Log.d(TAG, "oncomplete: that email is already in use");
                                                Toast.makeText(getActivity(), "That email is already in use", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d(TAG, "oncomplete: that email is available");
                                                /// the email is available so update it

                                                mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User email address updated.");
                                                                    Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
                                                                    mFirebaseMethods.updateEmail(mEmail.getText().toString());

                                                                }
                                                            }
                                                        });
                                            }

                                        } catch (NullPointerException e) {
                                            Log.d(TAG, " oncomplete :NullPointerException" + e.getMessage());

                                        }

                                    }

                                }
                            });


                        } else {
                            Log.d(TAG, "User re-authenticated. failed ");


                        }
                    }
                });

    }

    private static final String TAG = "EditProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private String userID;


    // edit profile fragment widget
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;
    private UserSettings mUserSettings;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        mDisplayName = view.findViewById(R.id.display_name);
        mUsername = view.findViewById(R.id.username);
        mDescription = view.findViewById(R.id.description);
        mEmail = view.findViewById(R.id.email);
        mWebsite = view.findViewById(R.id.website);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto);
        mFirebaseMethods = new FirebaseMethods(getActivity());


        setupFirebaseAuth();


//        initImageLoader();
//        setProfileImage(); /// defaylt image show
        //// back arrow for back to profile activity
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        ImageView checkmark = view.findViewById(R.id.saveChange);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick : attempting to save changes");
                saveProfileSettings();
                Toast.makeText(getContext(), " Changed ", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


//    private void initImageLoader(){
//        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
//        ImageLoader.getInstance().init(universalImageLoader.getConfig());
//
//    }


    /// for demo image showing
//    private  void setProfileImage(){
//        ////dono use https:// in image url because this is appended already
//        String imgUrl = "cdn.pixabay.com/photo/2015/10/18/18/07/android-994910_960_720.jpg";
//        UniversalImageLoader.setImage(imgUrl,mProfilePhoto,null,"https://");
//    }


    /*
    retrieving the data contained in the widget and submit it to the database
    defore doing so it checks  to make sure the username chosen is unique
     */
    private void saveProfileSettings() {
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();

        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


//                 User user = new User();
//                 for (DataSnapshot ds:dataSnapshot.child(getString(R.string.dbname_users)).getChildren()){
//                     if(ds.getKey().equals(userID)){
//                         user.setUsername(ds.getValue(User.class).getUsername());
//
//                     }
//                 }
//                Log.d(TAG,"ondatachanged : current username"+user.getUsername());


        //case1 if the user made  change their username
        if (!mUserSettings.getUser().getUsername().equals(username)) {
            checkIfUsernameExists(username);

        }
        //case2  if the user made  change their email

        if (!mUserSettings.getUser().getEmail().equals(email)) {
            // step1) Re athenticate
            // comfirm the password and email
            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
            dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));

            dialog.setTargetFragment(EditProfileFragment.this, 1);


            //step2) chek if the email is already registered
            // fetchprovideremail(String email)
            // step3) change the email
            // submit to the new email to database and authentication

        }


        /********************************************************************************************************************
         change the rest of the settings that do not require uniqueness
         */
        if (!mUserSettings.getSettings().getDisplay_name().equals(displayName)) {
            //udpate display name
            mFirebaseMethods.updateUserAccountSettings(displayName, null, null, 0);
        }

        if (!mUserSettings.getSettings().getWebsite().equals(website)) {
            //udpate website name
            mFirebaseMethods.updateUserAccountSettings(null, website, null, 0);

        }

        if (!mUserSettings.getSettings().getDescription().equals(description)) {
            //udpate description name
            mFirebaseMethods.updateUserAccountSettings(null, null, description, 0);

        }


        /// problem may occur
        if (!mUserSettings.getSettings().getProfile_photo().equals(phoneNumber)) {
            //udpate phone number
            mFirebaseMethods.updateUserAccountSettings(null, null, null, phoneNumber);


        }

    }


//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//    }

    //// check username is already exist in the database
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkusernameifexists:,checking if" + username + "already exist");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // add the username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "saved username", Toast.LENGTH_SHORT).show();

                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Log.d(TAG, "chekifusernameisexist, found a match " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username is already exists", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setProfileWidgets(UserSettings userSettings) {
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getEmail());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getPhone_number());

        mUserSettings = userSettings;

        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));

        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick:changing profile photo");
                Toast.makeText(getActivity(), "Photo changed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 268435456
                getActivity().startActivity(intent);
                getActivity().finish();

            }
        });

    }


      /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();


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

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

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










