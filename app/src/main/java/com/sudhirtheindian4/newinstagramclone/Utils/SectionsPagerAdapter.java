package com.sudhirtheindian4.newinstagramclone.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sudhirtheindian4.newinstagramclone.share.GalleryFragment;

import java.util.ArrayList;
import java.util.List;

/*
class that starts fragment for tabs
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private  final List<Fragment> fragmentList = new ArrayList<>();
    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public  void addFragmment(Fragment fragment){
        fragmentList.add(fragment);

    }


}
