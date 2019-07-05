package com.bantu.lift.driver.implementer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bantu.lift.driver.R;
import com.bantu.lift.driver.adapter.GetRequestPullAdapter;
import com.bantu.lift.driver.constant.FunctionHelper;
import com.bantu.lift.driver.fragment.RequestPollFragment;
import com.bantu.lift.driver.modelclass.ActionRequestedPollModel.ActionOnRequestedPollModel;
import com.bantu.lift.driver.modelclass.RequestPollModelData.Datum;
import com.bantu.lift.driver.modelclass.RequestPollModelData.RequestPollModel;
import com.bantu.lift.driver.presenter.IGetRequestedPullPresenter;
import com.bantu.lift.driver.retrofit.ApiUtils;
import com.bantu.lift.driver.retrofit.IRestInterfaces;
import com.bantu.lift.driver.utils.SharedPreferenceConstants;
import com.bantu.lift.driver.view.IGetRequestPullView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRequestedPullPresenterImplementer implements IGetRequestedPullPresenter {
    RecyclerView recycler_view;
    List<Datum> data;
    TextView noRecard;
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
        recycler_view = view.findViewById(R.id.recycler_view);
        noRecard = view.findViewById(R.id.noRecard);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);

        FunctionHelper.showDialog(context, "Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Log.e("userId---",""+sharedPreferences.getString(SharedPreferenceConstants.userId, ""));
        Log.e("serviceKey---",""+sharedPreferences.getString(SharedPreferenceConstants.serviceKey, ""));
        Call<RequestPollModel> signInModelclassCall = iRestInterfaces.getRequestedPolls(sharedPreferences.getString(SharedPreferenceConstants.serviceKey, ""), sharedPreferences.getString(SharedPreferenceConstants.userId, ""));
        signInModelclassCall.enqueue(new Callback<RequestPollModel>() {
            @Override
            public void onResponse(Call<RequestPollModel> call, Response<RequestPollModel> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        // Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        data = response.body().getData();

                        if (data.size() > 0) {
                            recycler_view.setVisibility(View.VISIBLE);
                            noRecard.setVisibility(View.GONE);
                            getRequestPullAdapter = new GetRequestPullAdapter(context, data, requestPollFragment);
                            recycler_view.setAdapter(getRequestPullAdapter);
                        } else {
                            recycler_view.setVisibility(View.GONE);
                            noRecard.setVisibility(View.VISIBLE);
                        }

                    } else if (status_val == 2) {

                        FunctionHelper.dismissDialog();
                        Toast.makeText(context, "You have already login in other device", Toast.LENGTH_SHORT).show();
                        iGetRequestPullView.OnLoginError();
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
        FunctionHelper.showDialog(context, "Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<ActionOnRequestedPollModel> signInModelclassCall = iRestInterfaces.actionOnRequestedPoll(sharedPreferences.getString(SharedPreferenceConstants.serviceKey, ""), sharedPreferences.getString(SharedPreferenceConstants.userId, ""), requestId, pollId);
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

