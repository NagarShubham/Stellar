package coms.example.cwaa1.stellar.ManagerAssign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class DateTab extends FragmentStatePagerAdapter {

    private ArrayList tabdate;
    private String s;
    private String rootid;

    public DateTab(FragmentManager fm, ArrayList tabdate, String s, String rootid) {
        super(fm);
        this.tabdate = tabdate;
        this.s = s;
        this.rootid = rootid;
    }

    @Override
    public Fragment getItem(int i) {
       ChildFragment fragment = new ChildFragment();
       Bundle bundle = new Bundle();
       bundle.putString("day",tabdate.get(i).toString());
       bundle.putString("father",s);
       bundle.putString("fatherid",rootid);
       fragment.setArguments(bundle);
        return fragment;

    }


    @Override
    public int getCount() {
        return tabdate.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return tabdate.get(position).toString();
    }
}
