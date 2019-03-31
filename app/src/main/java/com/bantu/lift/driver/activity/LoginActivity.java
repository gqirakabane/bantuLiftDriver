package com.bantu.lift.driver.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bantu.lift.driver.R;
import com.bantu.lift.driver.implementer.LoginPresenterImplementer;
import com.bantu.lift.driver.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView ,View.OnClickListener{
    TextView tv_register_now,tv_forgot_passowrd;
    EditText et_userName, et_password;
    TextInputLayout txtInputUserName, txtInputPassword;
    Button loginBtn;
    LoginPresenterImplementer loginPresenterImplementer;
    private static final int PERMISSION_CAMERA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        View rootView = getWindow().getDecorView().getRootView();
        loginPresenterImplementer = new LoginPresenterImplementer(this, rootView, LoginActivity.this);
        et_userName = findViewById(R.id.et_userName);
        et_password = findViewById(R.id.et_password);
        txtInputUserName = findViewById(R.id.txtInputUserName);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        tv_register_now = findViewById(R.id.tv_register_now);
        tv_forgot_passowrd = findViewById(R.id.tv_forgot_passowrd);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        tv_forgot_passowrd.setOnClickListener(this);
        tv_register_now.setOnClickListener(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        txtInputUserName.setTypeface(typeface);
        txtInputPassword.setTypeface(typeface);

        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_CAMERA);
        }

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
        if (R.id.loginBtn==id)
        {
            loginPresenterImplementer.sendRequest();
        }  if (R.id.tv_register_now==id)
        {
            loginPresenterImplementer.sendSignUpRequest();
        }  if (R.id.tv_forgot_passowrd==id)
        {
            loginPresenterImplementer.sendForgotScreenRequest();
        }
    }
}
