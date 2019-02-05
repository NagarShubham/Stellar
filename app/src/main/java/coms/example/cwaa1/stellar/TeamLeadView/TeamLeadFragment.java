package coms.example.cwaa1.stellar.TeamLeadView;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import coms.example.cwaa1.stellar.LeadView.DetailsFragment;
import coms.example.cwaa1.stellar.Lead_List.AdapterLeadList;
import coms.example.cwaa1.stellar.Lead_List.LeadModel;
import coms.example.cwaa1.stellar.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TeamLeadFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "root";
    private static final String ARG_PARAM2 = "rootId";
    private Activity activity;
    private RecyclerView recyclerView;
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private List<LeadModel> list;

    // TODO: Rename and change types of parameters
    private String mParamName;
    private String mParamNameID;

    private OnFragmentInteractionListener mListener;

    public TeamLeadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamLeadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamLeadFragment newInstance(String param1, String param2) {
        TeamLeadFragment fragment = new TeamLeadFragment();
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
            mParamName = getArguments().getString(ARG_PARAM1);
            mParamNameID = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_lead, container, false);
      list=new ArrayList<>();
        if (getArguments() != null) {
            mParamName = getArguments().getString(ARG_PARAM1);
            mParamNameID = getArguments().getString(ARG_PARAM2);

            Log.e("id and name", mParamName + " " + mParamNameID);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, OrientationHelper.VERTICAL, false);
        recyclerView = view.findViewById(R.id.t_rec);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        apicall();


        return view;
    }

    private void apicall() {

        String key = getsha256("getAllLeadList");
        Log.e("key", key);
      //  progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAllLeadList")
                .add("key", key)
                .add("employee_id", mParamNameID)
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
                      //  progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("getapi Result",result);

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);

                        String msg = object.getString("message").trim();
                        Log.e("api message   ", msg);

                        if (msg.equals("true")) {


                            JSONArray array = object.getJSONArray("self");


                            if (array.length() > 0) {
                                list.clear();
                                Log.w("inside","api");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject objectname = array.getJSONObject(i);

                                    String id = objectname.getString("lead_id");
                                    String name = objectname.getString("name");
                                    String email = objectname.getString("email");
                                    String expected_closing_date = objectname.getString("expected_closing_date");
                                    String mobile = objectname.getString("mobile");
                                    String contact_type = objectname.getString("contact_type");
                                    String address = objectname.getString("address");
                                    String office_phone = objectname.getString("office_phone");
                                    String company_name = objectname.getString("company_name");
                                    String department = objectname.getString("department");
                                    String designation = objectname.getString("designation");
                                    String source = objectname.getString("sourceName");
                                    String description = objectname.getString("description");
                                    String type_of_requirement = objectname.getString("requirementName");
                                    String expected_lead_value = objectname.getString("expected_lead_value");
                                    String lead_type = objectname.getString("lead_type");

                                    String to_time = objectname.getString("to_time");
                                    String from_time = objectname.getString("from_time");
                                    String category = objectname.getString("categoryName");


                                    LeadModel model = new LeadModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setTotime(to_time);
                                    model.setFormtime(from_time);
                                    model.setDate(expected_closing_date);
                                    model.setMobile(mobile);
                                    model.setContacttype(contact_type);
                                    model.setAddress(address);
                                    model.setEmail(email);
                                    model.setOfficephoen(office_phone);
                                    model.setCompany_name(company_name);
                                    model.setDepartment(department);
                                    model.setDesignation(designation);
                                    model.setSource(source);
                                    model.setDescription(description);
                                    model.setType_of_requirement(type_of_requirement);
                                    model.setExpected_lead_value(expected_lead_value);
                                    model.setLead_type(lead_type);


                                    list.add(model);

                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        AdapterTeamLeadList adapterLeadList = new AdapterTeamLeadList(list);
                                        recyclerView.setAdapter(adapterLeadList);
                                                             // Toast.makeText(activity, "click", Toast.LENGTH_SHORT).show();



                                                adapterLeadList.setClickListener(new AdapterLeadList.ItemClickListener() {
                                                    @Override
                                                    public void onItemClick(int position) {

                                                        DetailsFragment detailsFragment = new DetailsFragment();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("id", list.get(position).getId());
                                                        bundle.putString("name", list.get(position).getName());
                                                        bundle.putString("email", list.get(position).getEmail());
                                                        bundle.putString("date", list.get(position).getDate());
                                                        bundle.putString("mob", list.get(position).getMobile());
                                                        bundle.putString("conty", list.get(position).getContacttype());
                                                        bundle.putString("add", list.get(position).getAddress());
                                                        bundle.putString("offno", list.get(position).getOfficephoen());
                                                        bundle.putString("comname", list.get(position).getCompany_name());
                                                        bundle.putString("dep", list.get(position).getDepartment());
                                                        bundle.putString("desig", list.get(position).getDesignation());
                                                        bundle.putString("descrip", list.get(position).getDescription());
                                                        bundle.putString("tor", list.get(position).getType_of_requirement());
                                                        bundle.putString("lvalue", list.get(position).getExpected_lead_value());
                                                        bundle.putString("ltype", list.get(position).getLead_type());

                                                        bundle.putString("source", list.get(position).getSource());
                                                        bundle.putString("cat", list.get(position).getCategory());
                                                        bundle.putString("toti", list.get(position).getTotime());
                                                        bundle.putString("fti", list.get(position).getFormtime());

                                                        detailsFragment.setArguments(bundle);
                                                        loadFragment(detailsFragment);


                                                    }

                                            @Override
                                            public void onFollowup(int i) {

                                            }

                                            @Override
                                            public void onCall(int i) {

                                            }
                                        });


                                    }

                                });

                            }
                        }
                    }catch (Exception e) {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
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
                Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadFragment(Fragment fragment) {

        if (activity.getFragmentManager() != null) {
            FragmentTransaction transaction = ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fream, fragment).addToBackStack(null);
            transaction.commit();
        }

    }

}
