package coms.example.cwaa1.stellar.TeamLeadView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import coms.example.cwaa1.stellar.ManagerAssign.RequestListFragment;
import coms.example.cwaa1.stellar.ManagerAssign.TabIdModel;

public class TeamAdapter extends FragmentStatePagerAdapter {

    private ArrayList<TabIdModel> tabn;

    public TeamAdapter(FragmentManager fm, ArrayList<TabIdModel> tabn) {
        super(fm);
        this.tabn = tabn;
    }

    @Override
    public Fragment getItem(int i) {
        TeamLeadFragment fragment = new TeamLeadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("root",tabn.get(i).getName().toString());
        bundle.putString("rootId",tabn.get(i).getNameId());
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
        //Log.e("pageTitle",tabn.get(position).toString());
        return tabn.get(position).getName().toString();
    }


}
