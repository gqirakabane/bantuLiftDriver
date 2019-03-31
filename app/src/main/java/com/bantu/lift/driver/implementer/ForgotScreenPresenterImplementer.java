package com.bantu.lift.driver.implementer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bantu.lift.driver.R;
import com.bantu.lift.driver.activity.ForgotPasswordScreen;
import com.bantu.lift.driver.activity.LoginActivity;
import com.bantu.lift.driver.constant.CommonMeathod;
import com.bantu.lift.driver.constant.FunctionHelper;
import com.bantu.lift.driver.modelclass.ForgotModel.ForgotModelclass;
import com.bantu.lift.driver.presenter.IForgotScreenPresenter;
import com.bantu.lift.driver.retrofit.ApiUtils;
import com.bantu.lift.driver.retrofit.IRestInterfaces;
import com.bantu.lift.driver.view.IForgotScreenView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bantu.lift.driver.utils.GlobalValidation.isEmailValid;

public class ForgotScreenPresenterImplementer implements IForgotScreenPresenter {

    IForgotScreenView iForgotScreenView;
    View view;
    private Context context;

    public ForgotScreenPresenterImplementer(IForgotScreenView context, View view, Context context1) {
        this.iForgotScreenView = context;
        this.context = context1;
        iForgotScreenView.OnInitView(view);
        this.view = view;
    }
    @Override
    public void sendRequest() {
        CommonMeathod.hideKeyboard(context);
        if (validationForgotCheck() == true) {
            EditText et_email;
            final String email;


            et_email = view.findViewById(R.id.et_email);
            FunctionHelper.showDialog(context,"Loading...");
            IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
            Call<ForgotModelclass> signInModelclassCall = iRestInterfaces.forgotPassword(et_email.getText().toString());
            signInModelclassCall.enqueue(new Callback<ForgotModelclass>() {
                @Override
                public void onResponse(Call<ForgotModelclass> call, Response<ForgotModelclass> response) {
                    if (response.isSuccessful()) {
                        FunctionHelper.dismissDialog();
                        int status_val = Integer.parseInt(response.body().getErrorCode());
                        if (status_val == 0) {

                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                            ((ForgotPasswordScreen) context).finish();

                        } else if (status_val == 1) {
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ForgotModelclass> call, Throwable t) {
                    FunctionHelper.dismissDialog();

                }
            });

        }

    }

    @Override
    public void sendBackRequest() {
        ((ForgotPasswordScreen) context).finish();

    }

    @SuppressLint("NewApi")
    public boolean validationForgotCheck() {
        boolean check = false;

        EditText et_email;
        final String email;


        et_email = view.findViewById(R.id.et_email);
        email = et_email.getText().toString();

        if (check == false) {

          if (email.equals("")) {


                Toast.makeText(context, "Please enter email id", Toast.LENGTH_SHORT).show();

            } else if ((isEmailValid(email) == false)) {


                Toast.makeText(context, "Please enter valid email id", Toast.LENGTH_SHORT).show();

            } else {

                check = true;
            }
        }
        return check;
    }
}
