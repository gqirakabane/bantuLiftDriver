package com.bantu.lift.driver.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bantu.lift.driver.R;
import com.bantu.lift.driver.adapter.SpinnerDropdownAdapter;
import com.bantu.lift.driver.implementer.SignUpPresenterImplementer;
import com.bantu.lift.driver.view.ISignupView;

import java.util.ArrayList;
import java.util.List;

public class SignupScreen extends AppCompatActivity implements ISignupView,View.OnClickListener {
    TextInputLayout txtInputfullname, txtInputmobile,txtInputemail,txtInputgender;
    TextInputLayout txtInputwork, txtInputhomecity,txtInputPassword,txtInputcpassword;
    SignUpPresenterImplementer signUpPresenterImplementer;
    Button signupBtn;
    TextView tv_gotologin;
    ImageView back;
    SpinnerDropdownAdapter spinnerDropdownAdapter;
    List<String> genderList=new ArrayList<>();
    Spinner spinner_carType;
    int flag=0;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        View rootView = getWindow().getDecorView().getRootView();
        signUpPresenterImplementer = new SignUpPresenterImplementer(this, rootView, SignupScreen.this);
        txtInputcpassword=findViewById(R.id.txtInputcpassword);
        txtInputPassword=findViewById(R.id.txtInputPassword);
        txtInputhomecity=findViewById(R.id.txtInputhomecity);
        txtInputwork=findViewById(R.id.txtInputwork);
        txtInputfullname=findViewById(R.id.txtInputfullname);
        txtInputmobile=findViewById(R.id.txtInputmobile);
        txtInputemail=findViewById(R.id.txtInputemail);
        txtInputgender=findViewById(R.id.txtInputgender);
        signupBtn=findViewById(R.id.signupBtn);
        tv_gotologin=findViewById(R.id.tv_gotologin);
        back=findViewById(R.id.back);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        txtInputcpassword.setTypeface(typeface);
        txtInputPassword.setTypeface(typeface);
        txtInputhomecity.setTypeface(typeface);
        txtInputwork.setTypeface(typeface);
        txtInputfullname.setTypeface(typeface);
        txtInputmobile.setTypeface(typeface);
        txtInputemail.setTypeface(typeface);
        txtInputgender.setTypeface(typeface);
        signupBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        tv_gotologin.setOnClickListener(this);
        spinner_carType=findViewById(R.id.spinner_carType);
        genderList.clear();
        genderList.add("Gender");
        genderList.add("Male");
        genderList.add("Female");
        spinnerDropdownAdapter = new SpinnerDropdownAdapter(SignupScreen.this, genderList);
        spinner_carType.setAdapter(spinnerDropdownAdapter);
        spinner_carType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (genderList.get(position).equalsIgnoreCase("Gender"))
                {
                    flag=0;
                }else if (genderList.get(position).equalsIgnoreCase("Male"))
                {
                    flag=1;
                }else {
                    flag=2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void OnSignUpSuccess() {

    }

    @Override
    public void OnSignUpinError() {

    }

    @Override
    public void OnInitView(View view) {

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (R.id.signupBtn==id)
        {
            signUpPresenterImplementer.signUpSendRequest(flag);
        }
        if (R.id.tv_gotologin==id)
        {
            signUpPresenterImplementer.sendLoginRequest();
        } if (R.id.back==id)
        {
            signUpPresenterImplementer.sendLoginRequest();
        }

    }
}
