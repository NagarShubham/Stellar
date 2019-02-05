package coms.example.cwaa1.stellar.ManagerAssign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

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
        bundle.putString("root",tabn.get(i).getName().toString());
        bundle.putString("rootId",tabn.get(i).getNameId().toString());
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
