package com.example.saurabh.driver.implementer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.saurabh.driver.R;
import com.example.saurabh.driver.adapter.GetRequestPullAdapter;
import com.example.saurabh.driver.adapter.NotificationAdapter;
import com.example.saurabh.driver.constant.FunctionHelper;
import com.example.saurabh.driver.fragment.RequestPollFragment;
import com.example.saurabh.driver.modelclass.ActionRequestedPollModel.ActionOnRequestedPollModel;
import com.example.saurabh.driver.modelclass.RequestPollModelData.Datum;
import com.example.saurabh.driver.modelclass.RequestPollModelData.RequestPollModel;
import com.example.saurabh.driver.presenter.IGetRequestedPullPresenter;
import com.example.saurabh.driver.presenter.INotificationlPresenter;
import com.example.saurabh.driver.retrofit.ApiUtils;
import com.example.saurabh.driver.retrofit.IRestInterfaces;
import com.example.saurabh.driver.utils.SharedPreferenceConstants;
import com.example.saurabh.driver.view.IGetRequestPullView;
import com.example.saurabh.driver.view.INotificationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPresenterImplementer implements INotificationlPresenter {
    RecyclerView recycler_view;
    List<Datum> data;
    NotificationAdapter notificationAdapter;
    INotificationView iNotificationView;
    View view;
    private Context context;
    private SharedPreferences sharedPreferences;
    GetRequestPullAdapter getRequestPullAdapter;
    RequestPollFragment requestPollFragment;
    public NotificationPresenterImplementer(INotificationView context, View view, Context context1 ) {
        this.iNotificationView = context;
        this.context = context1;
        iNotificationView.OnInitView(view);
        this.view = view;
    }

    @Override
    public void sendRequest() {
        sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
        recycler_view=view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);

        FunctionHelper.showDialog(context,"Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<RequestPollModel> signInModelclassCall = iRestInterfaces.getRequestedPolls(sharedPreferences.getString(SharedPreferenceConstants.serviceKey,""), sharedPreferences.getString(SharedPreferenceConstants.userId,""));
        signInModelclassCall.enqueue(new Callback<RequestPollModel>() {
            @Override
            public void onResponse(Call<RequestPollModel> call, Response<RequestPollModel> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        // Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                        data=response.body().getData();
                        getRequestPullAdapter =new GetRequestPullAdapter(context,data,requestPollFragment);
                        recycler_view.setAdapter(getRequestPullAdapter);
                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();

                        Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<RequestPollModel> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }

    @Override
    public void getNotification() {
        sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
        recycler_view=view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);
        notificationAdapter=new NotificationAdapter(context);
        recycler_view.setAdapter(notificationAdapter);
    }



    @Override
    public void sendSignUpRequest() {

    }

}

