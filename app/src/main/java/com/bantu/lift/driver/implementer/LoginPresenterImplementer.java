package com.bantu.lift.driver.implementer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bantu.lift.driver.MainActivity;
import com.bantu.lift.driver.R;
import com.bantu.lift.driver.activity.ForgotPasswordScreen;
import com.bantu.lift.driver.activity.LoginActivity;
import com.bantu.lift.driver.activity.SignupScreen;
import com.bantu.lift.driver.constant.CommonMeathod;
import com.bantu.lift.driver.constant.FunctionHelper;
import com.bantu.lift.driver.modelclass.signmodelclass.SignInModelclass;
import com.bantu.lift.driver.presenter.ILoginPresenter;
import com.bantu.lift.driver.retrofit.ApiUtils;
import com.bantu.lift.driver.retrofit.IRestInterfaces;
import com.bantu.lift.driver.utils.SharedPreferenceConstants;
import com.bantu.lift.driver.view.ILoginView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenterImplementer implements ILoginPresenter {
    ILoginView iLoginView;
    View view;
    private SharedPreferences sharedPreferences;

    private Context context;
    public LoginPresenterImplementer(ILoginView context, View view, Context context1) {
        this.iLoginView = context;
        this.context = context1;
        iLoginView.OnInitView(view);
        this.view = view;
    }

    @Override
    public void sendRequest() {
        CommonMeathod.hideKeyboard(context);
        if (validationLoginCheck() == true) {
            //final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            EditText et_userName, et_password;
            final String email;
            et_userName = view.findViewById(R.id.et_userName);
            et_password = view.findViewById(R.id.et_password);
            email = et_userName.getText().toString();
            final String password = et_password.getText().toString();
            sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
            String refreshedToken = sharedPreferences.getString(SharedPreferenceConstants.fcmId,"");
          // Toast.makeText(context, refreshedToken, Toast.LENGTH_SHORT).show();
            FunctionHelper.showDialog(context,"Loading...");
            IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
            Call<SignInModelclass> signInModelclassCall = iRestInterfaces.signInUser(email, password,"CPS95ssmdeh7567njycsh","android",refreshedToken,"2");
            signInModelclassCall.enqueue(new Callback<SignInModelclass>() {
                @Override
                public void onResponse(Call<SignInModelclass> call, Response<SignInModelclass> response) {
                    if (response.isSuccessful()) {
                        FunctionHelper.dismissDialog();
                        int status_val = Integer.parseInt(response.body().getErrorCode());
                        if (status_val == 0) {
                            sharedPreferences.edit().putString(SharedPreferenceConstants.userId, response.body().getData().getUserId()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.email, response.body().getData().getEmail()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.mobile, response.body().getData().getMobile()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.serviceKey, response.body().getServiceKey()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.name, response.body().getData().getName()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.image, response.body().getData().getImage()).apply();

                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((LoginActivity) context).finish();

                        } else if (status_val == 1) {
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<SignInModelclass> call, Throwable t) {
                    FunctionHelper.dismissDialog();

                }
            });
        }

        }



    @Override
    public void sendForgotScreenRequest() {
        Intent intent = new Intent(context, ForgotPasswordScreen.class);
        context.startActivity(intent);

    }

    @Override
    public void sendSignUpRequest() {

        Intent intent = new Intent(context, SignupScreen.class);
        context.startActivity(intent);
    }

    public boolean validationLoginCheck() {
        boolean check = false;
        EditText et_userName, et_password;
        final String email;
        et_userName = view.findViewById(R.id.et_userName);
        et_password = view.findViewById(R.id.et_password);
        email = et_userName.getText().toString();
        final String password = et_password.getText().toString();


        if (check == false) {

            if (email.equals("")) {

                Toast.makeText(context, "Please enter user name", Toast.LENGTH_SHORT).show();

            }else if (password.equalsIgnoreCase("")) {

                Toast.makeText(context, "please enter password", Toast.LENGTH_SHORT).show();

            } else if (password.length()<5) {
                Toast.makeText(context, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {

                check = true;
            }
        }
        return check;
    }

}