package com.stellar.android.app.ManagerAssign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class NameTab_Adapter extends FragmentStatePagerAdapter {

    private ArrayList<TabIdModel> tabn;

    public NameTab_Adapter(FragmentManager fm, ArrayList<TabIdModel> tabn) {
        super(fm);
        this.tabn = tabn;
    }

    @Override
    public Fragment getItem(int i) {
        RequestListFragment fragment = new RequestListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("root", tabn.get(i).getName());
        bundle.putString("rootId", tabn.get(i).getNameId());
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
