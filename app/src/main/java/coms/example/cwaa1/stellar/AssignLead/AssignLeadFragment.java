package coms.example.cwaa1.stellar.AssignLead;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import coms.example.cwaa1.stellar.DashBordActivity;
import coms.example.cwaa1.stellar.HomeFragment;
import coms.example.cwaa1.stellar.Lead_List.LeadModel;
import coms.example.cwaa1.stellar.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class AssignLeadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String private_key = "qJB0rGtIn5UB1xG03efyCp";
    private SharedPreferences preferences;

    private String userId;
    String Bid, Tid;
    private String TempName;

    private List<String> listBy;
    private List<String> listByID;
    private List<String> listTo;
    private List<String> listToId;
    private String mParam1;
    private String mParam2;
    private Activity activity;
    private Spinner sp_by, sp_to;
    private Button btn_asign;
    private RecyclerView recyclerView;
    private List<LeadModel> rclist;
    private ArrayAdapter adapterby;
    private ArrayAdapter adapterto;
    private ArrayList idList;
    private ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;
    private String TAG = "AssignLeadFragment ";

    public AssignLeadFragment() {
    }


    public static AssignLeadFragment newInstance(String param1, String param2) {
        AssignLeadFragment fragment = new AssignLeadFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_lead, container, false);

        DashBordActivity.titleName("Assign Lead");

        listBy = new ArrayList<>();
        listByID = new ArrayList<>();
        listTo = new ArrayList<>();
        listToId = new ArrayList<>();
        rclist = new ArrayList<>();
        idList = new ArrayList<>();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        preferences = activity.getSharedPreferences("stelleruser", MODE_PRIVATE);
        userId = preferences.getString("empid", "");
        Log.e("AssignLead UID ", userId);

        sp_by = view.findViewById(R.id.lby);
        sp_to = view.findViewById(R.id.lto);
        btn_asign = view.findViewById(R.id.btn_assign);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, OrientationHelper.VERTICAL, false);
        recyclerView = view.findViewById(R.id.rec_assign);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        spinerApi(userId);


        sp_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, int position, final long id) {
                Bid = listByID.get(position).toString();
                TempName = listBy.get(position).toString();
                leadApi(Bid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Tid = listToId.get(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_asign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LeadBy ", Bid);
                Log.e("LeadTo ", Tid);
                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < idList.size(); i++) {
                    jsonArray.put(idList.get(i));
                }
                Log.e("Element Array", "" + jsonArray);
                Log.e("user iddss", "" + userId);

                assignApi(Tid, userId, jsonArray);

            }
        });


        return view;
    }

    private void assignApi(String tid, String userId, JSONArray jsonArray) {


        progressDialog.show();
        Log.e("Element Array", "" + jsonArray);
        Log.e("user iddss", "" + userId);


        String key = getsha256("leadAssign");
        Log.e("key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "leadAssign")
                .add("key", key)
                .add("assign_by", userId)
                .add("lead_id", jsonArray.toString())
                .add("assign_to_employee_id", tid)
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
                        progressDialog.dismiss();
                        Log.e("network", e.getMessage());
                        Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("Assign Lead result", result);
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);

                        String msg = object.getString("message");
                        Log.e("message   ", msg);

                        if (msg.equals("true")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   // progressDialog.dismiss();
                                    leadApi(Bid);

                                }
                            });
//                            HomeFragment leadListActivity = new HomeFragment();
//                            FragmentTransaction transaction = null;
//                            if (getFragmentManager() != null) {
//                                toast("Success");
//                                transaction = getFragmentManager().beginTransaction();
//                                transaction.replace(R.id.fream, leadListActivity);
//                                transaction.commit();
//                            }

                        } else {
                            toast("Not Assign Try Again");
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            }
        });


    }

    private void leadApi(final String bid) {
        Log.e(TAG + " Emp", bid);
        String key = getsha256("getAllLeadList");
        Log.e("key", key);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "getAllLeadList")
                .add("key", key)
                .add("employee_id", bid)
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
                        //progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("lead Result", result);

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(result);

                        String msg = object.getString("message").trim();
                        Log.e("api message   ", msg);

                        if (msg.equals("true")) {


                            JSONArray array = object.getJSONArray("self");
                            rclist.clear();
                            idList.clear();

                            if (array.length() > 0) {
                                rclist.clear();
                                idList.clear();
                                Log.w("inside", "api");

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
                                    String source = objectname.getString("source");
                                    String description = objectname.getString("description");
                                    String type_of_requirement = objectname.getString("requirementName");
                                    String expected_lead_value = objectname.getString("expected_lead_value");
                                    String lead_type = objectname.getString("lead_type");

                                    LeadModel model = new LeadModel();
                                    model.setId(id);
                                    model.setName(name);
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


                                    rclist.add(model);

                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //progressBar.setVisibility(View.GONE);

                                        AdapterAssignLead adapterAssignLead = new AdapterAssignLead(rclist);
                                        recyclerView.setAdapter(adapterAssignLead);
                                        adapterAssignLead.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                        // Toast.makeText(activity, "click", Toast.LENGTH_SHORT).show();

                                        adapterAssignLead.setClickListener(new AdapterAssignLead.ItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {


                                                if (((CheckedTextView) view).isChecked()) {

                                                    ((CheckedTextView) view).setChecked(false);
                                                    idList.remove(rclist.get(position).getId());
                                                    //  Toast.makeText(getContext(), "unchak "+LeadListActivity.chidnamelist.get(position).getName(), Toast.LENGTH_SHORT).show();
                                                } else {

                                                    // Toast.makeText(getContext(), "check "+LeadListActivity.chidnamelist.get(position).getName(), Toast.LENGTH_SHORT).show();
                                                    ((CheckedTextView) view).setChecked(true);
                                                    idList.add(rclist.get(position).getId());
                                                }


                                            }

                                            @Override
                                            public void onView(int position) {


                                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                // Get the layout inflater
                                                LayoutInflater inflater = activity.getLayoutInflater();
                                                // Inflate and set the layout for the dialog
                                                // Pass null as the parent view because its going in the dialog layout
                                                final View dView = inflater.inflate(R.layout.item_assign_detailsview, null);
                                                builder.setView(dView);
                                                final AlertDialog dialog = builder.create();

                                                TextView aname = dView.findViewById(R.id.a_name);
                                                TextView a_email = dView.findViewById(R.id.a_email);
                                                TextView a_mobile = dView.findViewById(R.id.a_mobile);
                                                TextView a_officeno = dView.findViewById(R.id.a_officeno);
                                                TextView a_company_name = dView.findViewById(R.id.a_company_name);
                                                TextView a_department = dView.findViewById(R.id.a_department);
                                                TextView a_designation = dView.findViewById(R.id.a_designation);
                                                TextView a_contact_type = dView.findViewById(R.id.a_contact_type);
                                                TextView a_type_of_requirement = dView.findViewById(R.id.a_type_of_requirement);
                                                TextView a_expected_lead_value = dView.findViewById(R.id.a_expected_lead_value);
                                                TextView a_lead_type = dView.findViewById(R.id.a_lead_type);
                                                TextView a_address = dView.findViewById(R.id.a_address);
                                                TextView a_description = dView.findViewById(R.id.a_description);


                                                a_email.setText(rclist.get(position).getEmail());
                                                a_mobile.setText(rclist.get(position).getMobile());
                                                a_contact_type.setText(rclist.get(position).getContacttype());
                                                a_address.setText(rclist.get(position).getAddress());
                                                a_officeno.setText(rclist.get(position).getOfficephoen());
                                                a_company_name.setText(rclist.get(position).getCompany_name());
                                                a_department.setText(rclist.get(position).getDepartment());
                                                a_designation.setText(rclist.get(position).getDesignation());
                                                aname.setText(rclist.get(position).getName());
                                                a_description.setText(rclist.get(position).getDescription());
                                                a_type_of_requirement.setText(rclist.get(position).getType_of_requirement());
                                                a_expected_lead_value.setText(rclist.get(position).getExpected_lead_value());
                                                a_lead_type.setText(rclist.get(position).getLead_type());


                                                dialog.show();
                                                //Toast.makeText(activity, "" + rclist.get(position).getName(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        toapi(Bid);
                                    }
                                });
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //progressBar.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                        toapi(Bid);
                                    }
                                });
                            }
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    toapi(Bid);

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            toapi(Bid);
                        }
                    });
                }


            }
        });


    }

    private void toapi(String bid) {
        String key = getsha256("assign_lead_dropdown");
        Log.e("assignlead_dropdown key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "assign_lead_dropdown")
                .add("key", key)
                .add("login_id", userId)
                .add("select_id", bid)
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
                        Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
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
                        if (msg.equals("True")) {
                            JSONArray array = object.getJSONArray("assign_by");
                            JSONArray arrayTo = object.getJSONArray("assign_to");
                            listTo.clear();
                            listToId.clear();
                            for (int i = 0; i < arrayTo.length(); i++) {
                                JSONObject objectnameTo = arrayTo.getJSONObject(i);
                                String full_name = objectnameTo.getString("full_name");
                                String employee_id = objectnameTo.getString("employee_id");
                                listTo.add(full_name);
                                listToId.add(employee_id);
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Log.e(TAG + "setAdapter", "Here");
                                    adapterto = new ArrayAdapter((FragmentActivity) activity, android.R.layout.simple_spinner_item, listTo);

                                    sp_to.setAdapter(adapterto);

                                }
                            });
                        } else {
                            toast("Try Again");
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                Log.e(TAG + "Result", result);
            }
        });

    }


    private void spinerApi(String bid) {


        String key = getsha256("assign_lead_dropdown");
        Log.e("assignlead_dropdown key", key);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "assign_lead_dropdown")
                .add("key", key)
                .add("login_id", userId)
                .add("select_id", bid)
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
                        Toast.makeText(activity, "network Problem", Toast.LENGTH_SHORT).show();
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
                        if (msg.equals("True")) {
                            JSONArray array = object.getJSONArray("assign_by");
                            JSONArray arrayTo = object.getJSONArray("assign_to");

                            listBy.clear();
                            listByID.clear();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objectname = array.getJSONObject(i);

                                String full_name = objectname.getString("full_name");

                                String employee_id = objectname.getString("employee_id");


                                listBy.add(full_name);
                                listByID.add(employee_id);


                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    adapterby = new ArrayAdapter((FragmentActivity) activity, android.R.layout.simple_spinner_item, listBy);
                                    sp_by.setAdapter(adapterby);
                                }
                            });
                        } else {
                            toast("Try Again");
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                Log.e(TAG + "Result", result);
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

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


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
}
