package com.sudhirtheindian4.newinstagramclone.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {


    private  final List<Fragment> mFragmentList = new ArrayList<>();

    private final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    private final HashMap<String, Integer> mFragmentsNumber = new HashMap<>();
    private final HashMap<Integer, String> mFragmentsNames = new HashMap<>();

   public SectionsStatePagerAdapter(FragmentManager fm){
       super(fm);
   }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public   void addFragment(Fragment  fragment,String fragmentName){
       mFragmentList.add(fragment);
       mFragments.put(fragment,mFragmentList.size()-1);
       mFragmentsNumber.put(fragmentName,mFragmentList.size()-1);
       mFragmentsNames.put(mFragmentList.size()-1,fragmentName);


    }
/*
return the fragments with the  name parameter
@frament name
 */
    public Integer getFragmentNumber(String fragmentName){
       if(mFragmentsNumber.containsKey(fragmentName)){
           return mFragmentsNumber.get(fragmentName);
       }
       else {
           return null;
       }
    }

/*
return the fragments with the  name parameter
@fragment
 */


    public Integer getFragmentNumber(Fragment fragment){
        if(mFragmentsNumber.containsKey(fragment)){
            return mFragmentsNumber.get(fragment);
        }
        else {
            return null;
        }
    }


    /*
return the fragments with the  name parameter
@fragment number
 */


    public String getFragmentName(Integer fragmentNumber){
        if(mFragmentsNames.containsKey(fragmentNumber)){
            return mFragmentsNames.get(fragmentNumber);
        }
        else {
            return null;
        }
    }




}
