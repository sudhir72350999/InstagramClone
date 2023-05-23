package com.sudhirtheindian4.newinstagramclone.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sudhirtheindian4.newinstagramclone.R;
import com.sudhirtheindian4.newinstagramclone.Utils.MainfeedListAdapter;
import com.sudhirtheindian4.newinstagramclone.models.Comment;
import com.sudhirtheindian4.newinstagramclone.models.Like;
import com.sudhirtheindian4.newinstagramclone.models.Photo;
import com.sudhirtheindian4.newinstagramclone.models.UserAccountSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    /// vars
    private ArrayList<Photo> mPhotos;
    private ArrayList<Photo> mPaginatedPhotos;
    private int mResults;

    private ArrayList<String> mFollowing;
    private ListView mListView;
    private MainfeedListAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mListView = view.findViewById(R.id.listView);
        mFollowing = new ArrayList<>();
        mPhotos = new ArrayList<>();

        getFollowing();
        return view;
    }

    private void getFollowing() {
        Log.d(TAG, "getFollowing: searaching for following ");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_following))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: found user" + singleSnapshot.child(getString(R.string.field_user_id)).getValue());
                    mFollowing.add(singleSnapshot.child(getString(R.string.field_user_id)).getValue().toString());


                }
                /// jo bhi user login hoga uska post show hoga
                mFollowing.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ///get the photo
                getPhotos();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getPhotos() {
        Log.d(TAG, "getPhotos: getting Photos");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        for (int i = 0; i < mFollowing.size(); i++) {
            final int count = i;
            Query query = reference
                    .child(getString(R.string.dbname_user_photos))
                    .child(mFollowing.get(i))
                    .orderByChild(getString(R.string.field_user_id))
                    .equalTo(mFollowing.get(i));


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                        Photo photo = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
                        photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                        photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                        photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                        photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());


                        ArrayList<Comment> comments = new ArrayList<Comment>();
                        for (DataSnapshot dSnapshot : singleSnapshot.child(getString(R.string.field_comments)).getChildren()) {
                            Comment comment = new Comment();
                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                            comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                            comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                            comments.add(comment);
                        }
                        photo.setComments(comments);
                        mPhotos.add(photo);

                    }

                    if (count >= mFollowing.size() - 1) {
                        // display our photos
                        displayPhoto();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }


    private void displayPhoto() {
        mPaginatedPhotos = new ArrayList<>();


        if (mPhotos != null) {

            try {
                Collections.sort(mPhotos, new Comparator<Photo>() {
                    @Override
                    public int compare(Photo o1, Photo o2) {
                        return o2.getDate_created().compareTo(o1.getDate_created());
                    }
                });

                int iteration = mPhotos.size();
                if (iteration > 10) {
                    iteration = 10;
                }
//                mResults =0; /// for infinite times photos showiing
                mResults = 10;
                for (int i = 0; i < iteration; i++) {
                    mPaginatedPhotos.add(mPhotos.get(i));
                }

                mAdapter = new MainfeedListAdapter(getActivity(), R.layout.layout_mainfeed_listitem, mPaginatedPhotos);
                mListView.setAdapter(mAdapter);
            } catch (NullPointerException e) {
                Log.d(TAG, "displayPhotos:NullPointerException " + e.getMessage());

            } catch (IndexOutOfBoundsException e) {
                Log.d(TAG, "displayPhotos:IndexOutOfBoundsException " + e.getMessage());

            }


        }
    }

    public void displayMorePhotos() {
        Log.d(TAG, "displayMorePhotos: displaying more photos");

        try {
            if (mPhotos.size() > mResults && mPhotos.size() > 0) {
                int iteration;
                if (mPhotos.size() > (mResults + 10)) {
                    Log.d(TAG, "displaying: there is greater than 10 more photos");
                    iteration = 10;

                } else {
                    Log.d(TAG, "displaying: there is less than 10 more photos");
                    iteration = mPhotos.size() - mResults;

                }

                //// add the new photos to the paginated results
                for (int i = 0; i < mResults + iteration; i++) {
                    mPaginatedPhotos.add(mPhotos.get(i));

                }
                mResults = mResults + iteration;
                mAdapter.notifyDataSetChanged();

            }

        } catch (NullPointerException e) {
            Log.d(TAG, "displayPhotos:NullPointerException " + e.getMessage());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "displayPhotos:IndexOutOfBoundsException " + e.getMessage());

        }

    }
}
