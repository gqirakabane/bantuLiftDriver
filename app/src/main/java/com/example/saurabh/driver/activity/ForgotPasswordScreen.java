package com.example.saurabh.driver.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.saurabh.driver.R;
import com.example.saurabh.driver.implementer.ForgotScreenPresenterImplementer;
import com.example.saurabh.driver.view.IForgotScreenView;

public class ForgotPasswordScreen extends AppCompatActivity implements IForgotScreenView,View.OnClickListener {
    EditText et_email;
    TextInputLayout txtInputemail;
    ImageView back;
    Button submitbtn;
    ForgotScreenPresenterImplementer forgotScreenPresenterImplementer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass_activity);
        View rootView = getWindow().getDecorView().getRootView();
        forgotScreenPresenterImplementer = new ForgotScreenPresenterImplementer(this, rootView, ForgotPasswordScreen.this);
        et_email = findViewById(R.id.et_email);
        txtInputemail = findViewById(R.id.txtInputemail);
        back = findViewById(R.id.back);
        submitbtn = findViewById(R.id.submitbtn);
         Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        txtInputemail.setTypeface(typeface);
        submitbtn.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (R.id.submitbtn==id)
        {
            forgotScreenPresenterImplementer.sendRequest();
        }

    }
}
