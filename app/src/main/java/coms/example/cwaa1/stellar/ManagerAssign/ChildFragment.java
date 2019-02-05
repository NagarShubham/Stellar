package coms.example.cwaa1.stellar.ManagerAssign;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import coms.example.cwaa1.stellar.Lead_List.LeadListActivity;
import coms.example.cwaa1.stellar.R;
import coms.example.cwaa1.stellar.TargetList.TargetAdapter;
import coms.example.cwaa1.stellar.TargetList.TargetModel;
import coms.example.cwaa1.stellar.TargetList.Target_ListActivity;
import coms.example.cwaa1.stellar.Task_List.AdapterList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChildFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    static String root;

    public ChildFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChildFragment.
     */
    // TODO: Rename and change types and number of parameters

    private RecyclerView recy_name;
    private ArrayList<DetailsModel> list;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private String res;
    private String takid;
private Activity activity;

    public static ChildFragment newInstance(String param1, String param2) {
        ChildFragment fragment = new ChildFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        TextView textView = view.findViewById(R.id.day);
        recy_name = view.findViewById(R.id.recy_name);



        list = new ArrayList<>();


        String d = getArguments().getString("day");
        String name = getArguments().getString("father");
        String rootId = getArguments().getString("fatherid");

     String fmoth;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        if (currentMonth<10)
        {
            fmoth="0"+currentMonth;
        }else {
            fmoth=String.valueOf(currentMonth);
        }


        String cur = currentYear + "-" + fmoth + "-" + d;

        textView.setText(name + " _ " + rootId + " _ " + cur);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        recy_name.setLayoutManager(linearLayoutManager);
        recy_name.setItemAnimator(new DefaultItemAnimator());

        api(rootId, cur);


        return view;
    }

    private void api(final String rootId, final String cur) {


        String key = getsha256("approval_task_list_by_parent");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "approval_task_list_by_parent")
                .add("key", key)
                .add("employee_id", rootId)
                .add("date", cur)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("network", e.getMessage());
                        Toast.makeText(getContext(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                Log.e("List of app stask ",cur+"   "+ result);

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        Log.e("message   ", object.getString("message"));
                        Log.e("message   ", cur+" "+rootId);

                        String msg = object.getString("message");

                        if (msg.equals("true")) {
                            JSONArray array = object.getJSONArray("data");

                            list.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objectname = array.getJSONObject(i);

                                String id = objectname.getString("id");
                                String plan_type = objectname.getString("plan_type");
                                String customer_name = objectname.getString("customer_name");
                                String mobile = objectname.getString("mobile");
                                String address = objectname.getString("address");
                                String details = objectname.getString("details");

                                DetailsModel model = new DetailsModel();

                                model.setId(id);
                                model.setContacttype(plan_type);
                                model.setName(customer_name);
                                model.setMobile(mobile);
                                model.setDetails(details);
                                model.setAddress(address);

                                list.add(model);

                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AdapterTab_Llist adapter = new AdapterTab_Llist(list);
                                    recy_name.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                    adapter.setClickListener(new AdapterTab_Llist.ItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {

                                            //  Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
                                            takid = list.get(position).getId().toString();

                                            res = "approved";
                                            update(res, takid);

                                        }

                                        @Override
                                        public void onCanleClick(int i) {

                                            //Toast.makeText(getContext(), "nooo", Toast.LENGTH_SHORT).show();

                                            takid = list.get(i).getId().toString();
                                            res = "reject";
                                            update(res, takid);

                                        }
                                    });

                                }
                            });


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void update(String res, String takid) {


        String key = getsha256("approve_reject_task_by_parent");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "approve_reject_task_by_parent")
                .add("key", key)
                .add("task_id", takid)
                .add("task_status", res)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.link))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("network", e.getMessage());
                        Toast.makeText(getContext(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String result = response.body().string();
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);
                        Log.e("message   ", object.getString("message"));

                        String msg = object.getString("message");

                        if (msg.equals("Task Update Successfully")) {
                            toast("Task update success");
                        } else {
                            toast("Reject");
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });


    }


    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public static String getsha256(String text) {
        String input = text.concat(private_key);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md != null) {
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0".concat(hashtext);
            }
            return hashtext;
        } else return "";
    }

    private void toast(final String s) {

       activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    this.activity=activity;
    }
}
