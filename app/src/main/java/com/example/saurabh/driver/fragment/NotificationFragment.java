package com.example.saurabh.driver.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saurabh.driver.MainActivity;
import com.example.saurabh.driver.R;
import com.example.saurabh.driver.adapter.NotificationAdapter;
import com.example.saurabh.driver.implementer.NotificationPresenterImplementer;
import com.example.saurabh.driver.view.INotificationView;

public class NotificationFragment extends Fragment implements View.OnClickListener, INotificationView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    NotificationPresenterImplementer notificationPresenterImplementer;
    RecyclerView recycler_view;
    NotificationAdapter notificationAdapter;
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        notificationPresenterImplementer = new NotificationPresenterImplementer(this, view, getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_view = view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);
        MainActivity.tootlbarheader.setVisibility(View.GONE);
        MainActivity.text_toolbarTitle.setVisibility(View.VISIBLE);
        MainActivity.text_toolbarTitle.setText("Notifications");
        notificationPresenterImplementer.getNotification();
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

    }

    @Override
    public void OnInitView(View view) {

    }
}
