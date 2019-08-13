package com.celio.fac.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.celio.fac.FragmentFour;
import com.celio.fac.FragmentOne;
import com.celio.fac.FragmentThree;
import com.celio.fac.FragmentTwo;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public final int TOTAL_PAGES = 4;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new FragmentOne();
            case 1:
                return new FragmentTwo();
            case 2:
                return new FragmentThree();
            case 3:
                return new FragmentFour();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return TOTAL_PAGES;
    }
}
