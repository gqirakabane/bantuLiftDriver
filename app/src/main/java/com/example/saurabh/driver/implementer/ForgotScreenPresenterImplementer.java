package com.example.saurabh.driver.implementer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saurabh.driver.R;
import com.example.saurabh.driver.activity.ForgotPasswordScreen;
import com.example.saurabh.driver.activity.LoginActivity;
import com.example.saurabh.driver.presenter.IForgotScreenPresenter;
import com.example.saurabh.driver.view.IForgotScreenView;

import static com.example.saurabh.driver.utils.GlobalValidation.isEmailValid;

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
        if (validationForgotCheck() == true) {

            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((ForgotPasswordScreen) context).finish();
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
