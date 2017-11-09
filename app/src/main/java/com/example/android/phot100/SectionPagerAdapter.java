package com.example.android.phot100;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.phot100.MainFragments.Camera;
import com.example.android.phot100.MainFragments.Friends;
import com.example.android.phot100.MainFragments.Requests;


class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Requests requestsFragment = new Requests();
                return requestsFragment;
            case 1:
                Camera cameraFragment = new Camera();
                return cameraFragment;
            case 2:
                Friends friendsFragment = new Friends();
                return friendsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "Requests";
            case 1:
                return "Camera";
            case 2:
                return "Friends";
            default:
                return null;
        }
    }
}
