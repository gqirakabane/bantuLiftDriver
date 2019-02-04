package com.example.saurabh.driver.implementer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saurabh.driver.MainActivity;
import com.example.saurabh.driver.R;
import com.example.saurabh.driver.activity.SignupScreen;
import com.example.saurabh.driver.activity.UploadLicenceActivity;
import com.example.saurabh.driver.constant.CommonMeathod;
import com.example.saurabh.driver.constant.FunctionHelper;
import com.example.saurabh.driver.modelclass.signupmodel.SignUpModelclass;
import com.example.saurabh.driver.presenter.ILicensePresenter;
import com.example.saurabh.driver.presenter.ISignUpPresenter;
import com.example.saurabh.driver.retrofit.ApiUtils;
import com.example.saurabh.driver.retrofit.IRestInterfaces;
import com.example.saurabh.driver.utils.SharedPreferenceConstants;
import com.example.saurabh.driver.view.ILicenseUploadView;
import com.example.saurabh.driver.view.ISignupView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.saurabh.driver.utils.GlobalValidation.isEmailValid;

public class LicenseUploadPresenterImplementer implements ILicensePresenter {
    ILicenseUploadView iLicenseUploadView;
    View view;
    private Context context;
    private SharedPreferences sharedPreferences;

    public LicenseUploadPresenterImplementer(ILicenseUploadView context, View view, Context context1) {
        this.iLicenseUploadView = context;
        this.context = context1;
        iLicenseUploadView.OnInitView(view);
        this.view = view;
    }

    @SuppressLint("NewApi")
    public boolean validationSignupCheck() {
        boolean check = false;
        EditText et_fullName, et_mobile, et_email, et_gender, et_work, et_homecity, et_password, et_cpassword;
        final String email;
        et_fullName = view.findViewById(R.id.et_fullName);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_email = view.findViewById(R.id.et_email);
        et_gender = view.findViewById(R.id.et_gender);
        et_work = view.findViewById(R.id.et_work);
        et_homecity = view.findViewById(R.id.et_homecity);
        et_cpassword = view.findViewById(R.id.et_cpassword);
        et_password = view.findViewById(R.id.et_password);
        email = et_email.getText().toString();
        final String fullName = et_fullName.getText().toString();
        final String mobile = et_mobile.getText().toString();
        final String gender = et_gender.getText().toString();
        final String work = et_work.getText().toString();
        final String homecity = et_homecity.getText().toString();
        final String password = et_password.getText().toString();
        final String cnf_password = et_cpassword.getText().toString();
        if (check == false) {
            if (fullName.equals("")) {
                Toast.makeText(context, "Please enter full name", Toast.LENGTH_SHORT).show();
            } else if (mobile.equals("")) {

                Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show();

            } else if (mobile.length() < 9) {

                Toast.makeText(context, "Phone number is not valid", Toast.LENGTH_SHORT).show();

            } else if (email.equals("")) {


                Toast.makeText(context, "Please enter email id", Toast.LENGTH_SHORT).show();

            } else if ((isEmailValid(email) == false)) {


                Toast.makeText(context, "Please enter valid email id", Toast.LENGTH_SHORT).show();

            } else if (gender.equals("")) {

                Toast.makeText(context, "please enter gender", Toast.LENGTH_SHORT).show();

            } else if (work.equals("")) {

                Toast.makeText(context, "please enter work city", Toast.LENGTH_SHORT).show();

            } else if (homecity.equals("")) {

                Toast.makeText(context, "please enter home city", Toast.LENGTH_SHORT).show();

            } else if (password.equals("")) {

                Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show();

            } else if (password.length() < 5) {

                Toast.makeText(context, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();

            } else if (cnf_password.equals("")) {

                Toast.makeText(context, "Enter your confirmation password", Toast.LENGTH_SHORT).show();

            } else if (!(password.equals(cnf_password))) {
                Toast.makeText(context, "password mismatch", Toast.LENGTH_SHORT).show();

            } else {

                check = true;
            }
        }
        return check;
    }

    @SuppressLint("NewApi")
    public boolean validationSignupCheck1(String photo, String photoid, String license) {
        boolean check = false;

        if (check == false) {

            if (photo.equalsIgnoreCase("photo")) {

                Toast.makeText(context, "Please upload photo ", Toast.LENGTH_SHORT).show();

            } else if (photoid.equalsIgnoreCase("photoid")) {

                Toast.makeText(context, "Please upload id ", Toast.LENGTH_SHORT).show();

            } else if (license.equalsIgnoreCase("photolicense")) {

                Toast.makeText(context, "Please upload license", Toast.LENGTH_SHORT).show();

            } else {

                check = true;
            }
        }
        return check;
    }

    @Override
    public void sendRequest() {
        if (validationSignupCheck() == true) {
            EditText et_fullName, et_mobile, et_email, et_gender, et_work, et_homecity, et_password, et_cpassword;
            final String email;
            sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

            et_fullName = view.findViewById(R.id.et_fullName);
            et_mobile = view.findViewById(R.id.et_mobile);
            et_email = view.findViewById(R.id.et_email);
            et_gender = view.findViewById(R.id.et_gender);
            et_work = view.findViewById(R.id.et_work);
            et_homecity = view.findViewById(R.id.et_homecity);
            et_cpassword = view.findViewById(R.id.et_cpassword);
            et_password = view.findViewById(R.id.et_password);
            email = et_email.getText().toString();
            final String fullName = et_fullName.getText().toString();
            final String mobile = et_mobile.getText().toString();
            final String gender = et_gender.getText().toString();
            final String work = et_work.getText().toString();
            final String homecity = et_homecity.getText().toString();
            final String password = et_password.getText().toString();
            final String cnf_password = et_cpassword.getText().toString();
            final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            FunctionHelper.showDialog(context, "Loading...");
            IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
            Call<SignUpModelclass> signInModelclassCall = iRestInterfaces.signupUser(fullName, email, mobile, gender, work, homecity, password, "CPS95ssmdeh7567njycsh", "android", refreshedToken, "2");
            signInModelclassCall.enqueue(new Callback<SignUpModelclass>() {
                @Override
                public void onResponse(Call<SignUpModelclass> call, Response<SignUpModelclass> response) {
                    if (response.isSuccessful()) {
                        FunctionHelper.dismissDialog();
                        int status_val = Integer.parseInt(response.body().getErrorCode());
                        if (status_val == 0) {
                            sharedPreferences.edit().putString(SharedPreferenceConstants.userId, response.body().getData().getUserId()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.email, response.body().getData().getEmail()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.mobile, response.body().getData().getMobile()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.serviceKey, response.body().getServiceKey()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.name, response.body().getData().getName()).apply();
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((SignupScreen) context).finish();
                        } else if (status_val == 1) {
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignUpModelclass> call, Throwable t) {
                    FunctionHelper.dismissDialog();

                }
            });
        }
    }

    @Override
    public void sendbackRequest() {
        ((UploadLicenceActivity) context).finish();
    }

    @Override
    public void sendSignUpRequest(String name, String mobile, String email, String gender, String city, String homeCity, String password, String photo, String photoid, String license) {
        CommonMeathod.hideKeyboard(context);

        if (validationSignupCheck1(photo, photoid, license) == true) {
            sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
            final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            File file = new File(photo);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

          File file1  = new File(photoid);
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part body1 = MultipartBody.Part.createFormData("identity", file.getName(), requestFile1);

          File file2 = new File(license);
            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("license", file.getName(), requestFile2);

            RequestBody name1 = RequestBody.create(MediaType.parse("multipart/form-data"),name );
            RequestBody mobile1 = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
            RequestBody email1 = RequestBody.create(MediaType.parse("multipart/form-data"), email);
            RequestBody gender1 = RequestBody.create(MediaType.parse("multipart/form-data"), gender);
            RequestBody city1 = RequestBody.create(MediaType.parse("multipart/form-data"), city);
            RequestBody homeCity1 = RequestBody.create(MediaType.parse("multipart/form-data"), homeCity);
            RequestBody password1 = RequestBody.create(MediaType.parse("multipart/form-data"), password);
            RequestBody serviceKey = RequestBody.create(MediaType.parse("multipart/form-data"), "CPS95ssmdeh7567njycsh");
            RequestBody deviceType = RequestBody.create(MediaType.parse("multipart/form-data"), "android");
            RequestBody fcmToken = RequestBody.create(MediaType.parse("multipart/form-data"), refreshedToken);
            RequestBody userType = RequestBody.create(MediaType.parse("multipart/form-data"), "2");

            FunctionHelper.showDialog(context, "Loading...");
            IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
            Call<SignUpModelclass> signInModelclassCall = iRestInterfaces.signupUser1(name1, email1, mobile1, gender1, city1, homeCity1, password1, serviceKey, deviceType, fcmToken, userType,body,body1,body2);
            signInModelclassCall.enqueue(new Callback<SignUpModelclass>() {
                @Override
                public void onResponse(Call<SignUpModelclass> call, Response<SignUpModelclass> response) {
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
                            ((UploadLicenceActivity) context).finish();
                        } else if (status_val == 1) {
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<SignUpModelclass> call, Throwable t) {
                    FunctionHelper.dismissDialog();
                }
            });

        }
    }
}
