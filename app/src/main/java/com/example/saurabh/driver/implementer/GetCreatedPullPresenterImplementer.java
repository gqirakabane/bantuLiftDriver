package com.example.saurabh.driver.implementer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.saurabh.driver.MainActivity;
import com.example.saurabh.driver.R;
import com.example.saurabh.driver.adapter.GetLiftAdapter;
import com.example.saurabh.driver.constant.FunctionHelper;
import com.example.saurabh.driver.modelclass.GetPullCreatedModelclass.GetPullModelclass;
import com.example.saurabh.driver.modelclass.LogoutModelclass.LogoutModelclass;
import com.example.saurabh.driver.presenter.ICreateLiftPresenter;
import com.example.saurabh.driver.presenter.IForgotScreenPresenter;
import com.example.saurabh.driver.presenter.IGetCreatedPullPresenter;
import com.example.saurabh.driver.retrofit.ApiUtils;
import com.example.saurabh.driver.retrofit.IRestInterfaces;
import com.example.saurabh.driver.utils.SharedPreferenceConstants;
import com.example.saurabh.driver.view.ICreateLiftView;
import com.example.saurabh.driver.view.IForgotScreenView;
import com.example.saurabh.driver.view.IGetCreatedPullView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCreatedPullPresenterImplementer implements IGetCreatedPullPresenter {
    RecyclerView recycler_view;

    IGetCreatedPullView iCreateLiftView;
    View view;
    private Context context;
    private SharedPreferences sharedPreferences;
    GetLiftAdapter getLiftAdapter;
    public GetCreatedPullPresenterImplementer(IGetCreatedPullView context, View view, Context context1) {
        this.iCreateLiftView = context;
        this.context = context1;
        iCreateLiftView.OnInitView(view);
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
        Call<GetPullModelclass> signInModelclassCall = iRestInterfaces.getMyPolls(sharedPreferences.getString(SharedPreferenceConstants.serviceKey,""), sharedPreferences.getString(SharedPreferenceConstants.userId,""));
        signInModelclassCall.enqueue(new Callback<GetPullModelclass>() {
            @Override
            public void onResponse(Call<GetPullModelclass> call, Response<GetPullModelclass> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                       // Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        getLiftAdapter =new GetLiftAdapter(context,response.body().getData());
                        recycler_view.setAdapter(getLiftAdapter);
                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();

                        Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<GetPullModelclass> call, Throwable t) {
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
}
