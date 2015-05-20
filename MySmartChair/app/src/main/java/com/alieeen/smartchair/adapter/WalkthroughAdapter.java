package com.alieeen.smartchair.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alieeen.smartchair.fragments.walktrough.FragmentWalkthrough01;
import com.alieeen.smartchair.fragments.walktrough.FragmentWalkthrough02;
import com.alieeen.smartchair.fragments.walktrough.FragmentWalkthrough03;
import com.alieeen.smartchair.fragments.walktrough.FragmentWalkthrough04;

/**
 * Created by alinekborges on 20/05/15.
 */

public class WalkthroughAdapter extends FragmentPagerAdapter {

    public WalkthroughAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return FragmentWalkthrough01.newInstance();
            case 1:
                return FragmentWalkthrough02.newInstance();
            case 2:
                return FragmentWalkthrough03.newInstance();
            case 3:
                return FragmentWalkthrough04.newInstance();
            default:
                return FragmentWalkthrough01.newInstance();
        }

        //return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return 4;
    }

    /*
    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }*/
}

