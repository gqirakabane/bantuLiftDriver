package com.bantu.lift.driver.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bantu.lift.driver.MainActivity;
import com.bantu.lift.driver.R;
import com.bantu.lift.driver.adapter.GetRequestPullAdapter;
import com.bantu.lift.driver.implementer.GetRequestedPullPresenterImplementer;
import com.bantu.lift.driver.interFace.AdapterCallback;
import com.bantu.lift.driver.utils.SharedPreferenceConstants;
import com.bantu.lift.driver.view.IGetRequestPullView;

public class RequestPollFragment extends Fragment implements View.OnClickListener, IGetRequestPullView,AdapterCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recycler_view;
    GetRequestedPullPresenterImplementer getRequestedPullPresenterImplementer;
    GetRequestPullAdapter getRequestPullAdapter;
    private String mParam1;
    private String mParam2;
TextView noRecard;
    SharedPreferences sharedPreferences;

    public RequestPollFragment() {
    }

    public static RequestPollFragment newInstance(String param1, String param2) {
        RequestPollFragment fragment = new RequestPollFragment();
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
        View view = inflater.inflate(R.layout.requestpoll_fragment, container, false);
        getRequestedPullPresenterImplementer = new GetRequestedPullPresenterImplementer(this, view, getActivity(),RequestPollFragment.this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_view = view.findViewById(R.id.recycler_view);
        sharedPreferences = getActivity().getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);
        getRequestedPullPresenterImplementer.sendRequest();
        MainActivity.tootlbarheader.setVisibility(View.GONE);
        MainActivity.text_toolbarTitle.setVisibility(View.VISIBLE);
        MainActivity.text_toolbarTitle.setText("Booking Requests");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.continue_map) {

        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void OnLoginSuccess() {

    }

    @Override
    public void OnLoginError() {
        logoutMeathod();
    }

    @Override
    public void OnInitView(View view) {

    }
    public  void ShowData()
    {
        Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(String pollId,String requestId,int position) {
        //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        getRequestedPullPresenterImplementer.actionOnRequestedPoll(pollId,requestId,position);

    }

    public  void logoutMeathod()
    {

        String refreshedToken = sharedPreferences.getString(SharedPreferenceConstants.fcmId, "");

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.email, "");
        editor.putString(SharedPreferenceConstants.name, "");
        editor.putString(SharedPreferenceConstants.serviceKey, "");
        editor.putString(SharedPreferenceConstants.userId, "");
        editor.putString(SharedPreferenceConstants.homeCity, "");
        editor.putString(SharedPreferenceConstants.workCity, "");
        editor.putString(SharedPreferenceConstants.mobile, "");
        editor.putString(SharedPreferenceConstants.checkPoll, "");
        editor.clear();
        editor.commit();
        sharedPreferences.edit().putString(SharedPreferenceConstants.fcmId, refreshedToken).apply();
        Intent i1 = new Intent();
        i1.setClassName("com.bantu.lift.driver", "com.bantu.lift.driver.activity.LoginActivity");
        i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);

    }
}
