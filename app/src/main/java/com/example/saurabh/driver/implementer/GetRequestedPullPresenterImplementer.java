package com.example.saurabh.driver.implementer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.saurabh.driver.R;
import com.example.saurabh.driver.adapter.GetRequestPullAdapter;
import com.example.saurabh.driver.constant.FunctionHelper;
import com.example.saurabh.driver.fragment.RequestPollFragment;
import com.example.saurabh.driver.modelclass.ActionRequestedPollModel.ActionOnRequestedPollModel;
import com.example.saurabh.driver.modelclass.RequestPollModelData.Datum;
import com.example.saurabh.driver.modelclass.RequestPollModelData.RequestPollModel;
import com.example.saurabh.driver.presenter.IGetRequestedPullPresenter;
import com.example.saurabh.driver.retrofit.ApiUtils;
import com.example.saurabh.driver.retrofit.IRestInterfaces;
import com.example.saurabh.driver.utils.SharedPreferenceConstants;
import com.example.saurabh.driver.view.IGetRequestPullView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRequestedPullPresenterImplementer implements IGetRequestedPullPresenter {
    RecyclerView recycler_view;
    List<Datum> data;

    IGetRequestPullView iGetRequestPullView;
    View view;
    private Context context;
    private SharedPreferences sharedPreferences;
    GetRequestPullAdapter getRequestPullAdapter;
    RequestPollFragment requestPollFragment;
    public GetRequestedPullPresenterImplementer(IGetRequestPullView context, View view, Context context1, RequestPollFragment requestPollFragment) {
        this.iGetRequestPullView = context;
        this.context = context1;
        this.requestPollFragment = requestPollFragment;
        iGetRequestPullView.OnInitView(view);
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
    public void sendForgotScreenRequest() {

    }

    @Override
    public void sendSignUpRequest() {

    }

    @Override
    public void actionOnRequestedPoll(String pollId, String requestId, final int postion) {
        sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
        FunctionHelper.showDialog(context,"Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<ActionOnRequestedPollModel> signInModelclassCall = iRestInterfaces.actionOnRequestedPoll(sharedPreferences.getString(SharedPreferenceConstants.serviceKey,""), sharedPreferences.getString(SharedPreferenceConstants.userId,""),requestId,pollId);
        signInModelclassCall.enqueue(new Callback<ActionOnRequestedPollModel>() {
            @Override
            public void onResponse(Call<ActionOnRequestedPollModel> call, Response<ActionOnRequestedPollModel> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                         Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        data.remove(postion);
                        // basicAdapter.notifyItemRemoved(deletepos);
                         getRequestPullAdapter.notifyDataSetChanged();
                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();
                        Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
            @Override
            public void onFailure(Call<ActionOnRequestedPollModel> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }
}

