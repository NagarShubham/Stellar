package com.stellar.android.app.ManagerAssign;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import com.stellar.android.app.R;

public class RequestListFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayoutday;
    private ArrayList<String> tabday;
    private DateTab dayadapter;
    private ViewPager viewPager;
    static String sta = "";


    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int numDays;


    public RequestListFragment() {
        // Required empty public constructor
    }


    public static RequestListFragment newInstance(String param1, String param2) {
        RequestListFragment fragment = new RequestListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        tabLayoutday = view.findViewById(R.id.tabbarday);
        viewPager = view.findViewById(R.id.pager);
        tabday = new ArrayList<>();


        Day(view);


        return view;
    }

    private void Day(View view) {

        getMonth();


        TextView textView = view.findViewById(R.id.name);
        String s = getArguments().getString("root");
        String rid = getArguments().getString("rootId");
        textView.setText(s);
        sta = s;

        Log.e("request page", textView.getText().toString());

        for (int i = 1; i <= numDays; i++) {
            tabday.add("" + i);
        }





        tabLayoutday.setTabGravity(TabLayout.GRAVITY_FILL);
        dayadapter = new DateTab(getChildFragmentManager(), tabday, s,rid);
        viewPager.setAdapter(dayadapter);
        tabLayoutday.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayoutday.getTabAt(currentDay - 1); // Count Starts From 0
        tab.select();

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutday));


        tabLayoutday.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void getMonth() {


        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) ;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Log.e("Current day", "" + currentDay);
        Log.e("Current moth ", "" + currentMonth);
        Log.e("Current year ", "" + currentYear);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, currentYear);
        calendar1.set(Calendar.MONTH, currentMonth);
        numDays = calendar1.getActualMaximum(Calendar.DATE);

        Log.e("no of day in month", " " + numDays);


        // Toast.makeText(getContext(),"Today's Date: " + currentYear+"/" + currentMonth +"/"+ currentDay, Toast.LENGTH_SHORT).show();


//        for (int i=1;i<numDays;i++)
//        {
//           String s= tabday.get(i).toString();
//
//           if (s.equals(String.valueOf(currentDay)))
//           {
//
//               Log.e("foun that",s);
//           }
//
//        }


    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
