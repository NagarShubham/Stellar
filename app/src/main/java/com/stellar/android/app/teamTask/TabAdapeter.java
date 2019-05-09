package com.stellar.android.app.teamTask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.stellar.android.app.Constants;
import java.util.ArrayList;


public class TabAdapeter extends FragmentStatePagerAdapter {

    private ArrayList<PandingModel> tabn;

    public TabAdapeter(FragmentManager fm, ArrayList<PandingModel> tabn) {
        super(fm);
        this.tabn = tabn;
    }

    @Override
    public Fragment getItem(int i) {
        PandingFragment fragment = new PandingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ID, tabn.get(i).getId());
        bundle.putString(Constants.KEY_NAME, tabn.get(i).getName());
        bundle.putString(Constants.KEY_DAY, tabn.get(i).getDay());
        fragment.setArguments(bundle);
        return fragment;

    }


    @Override
    public int getCount() {
        return tabn.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabn.get(position).getName();
    }


}
