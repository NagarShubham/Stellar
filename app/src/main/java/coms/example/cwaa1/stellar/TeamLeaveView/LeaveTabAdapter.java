package coms.example.cwaa1.stellar.TeamLeaveView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class LeaveTabAdapter  extends FragmentStatePagerAdapter {

    private ArrayList<LeaveIdModel> tabn;

    public LeaveTabAdapter(FragmentManager fm, ArrayList<LeaveIdModel> tabn) {
        super(fm);
        this.tabn = tabn;
    }

    @Override
    public Fragment getItem(int i) {
        TeamLeaveFragment fragment = new TeamLeaveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("root",tabn.get(i).getName().toString());
        bundle.putString("rootId",tabn.get(i).getId());
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
