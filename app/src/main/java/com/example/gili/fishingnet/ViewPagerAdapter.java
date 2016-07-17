package com.example.gili.fishingnet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Gili on 06/07/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private Boolean isGoogleApiClientCreated = false;

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case(0):
                return new SignInFragment();
            case(1):
                //return new WeatherFragment();
                break;
            case(2):
                //return new MeetingsFragment();
                break;
            case(5):
                return new WeatherFragment();
            case (6):
                return new ListViewFragment();
            default:
                return new TabFragment();
        }
        return new TabFragment();
        // Which Fragment should be dislpayed by the viewpager for the given position
        // In my case we are showing up only one fragment in all the three tabs so we are
        // not worrying about the position and just returning the TabFragment
    }

    @Override
    public int getCount() {
        return 7;       // As there are only 6 Tabs
    }

}
