package com.bantu.lift.driver.implementer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bantu.lift.driver.R;
import com.bantu.lift.driver.activity.ForgotPasswordScreen;
import com.bantu.lift.driver.activity.SignupScreen;
import com.bantu.lift.driver.constant.CommonMeathod;
import com.bantu.lift.driver.constant.FunctionHelper;
import com.bantu.lift.driver.modelclass.CreatepullModelclass.CreatePullModelclass;
import com.bantu.lift.driver.presenter.ICreateLiftPresenter;
import com.bantu.lift.driver.retrofit.ApiUtils;
import com.bantu.lift.driver.retrofit.IRestInterfaces;
import com.bantu.lift.driver.utils.SharedPreferenceConstants;
import com.bantu.lift.driver.view.ICreateLiftView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLiftPresenterImplementer implements ICreateLiftPresenter {
    ICreateLiftView iCreateLiftPresenter;
    View view;
    private SharedPreferences sharedPreferences;
    String smoking1;
    String distance;
    private Context context;
    String otherPreferaces, carNumber, carName;

    public CreateLiftPresenterImplementer(ICreateLiftView context, View view, Context context1) {
        this.context = context1;
        this.iCreateLiftPresenter = context;
        iCreateLiftPresenter.OnInitView(view);
        this.view = view;
    }

    @Override
    public void sendRequest(String img, double l1, double t1, double l2, double t2, String start_date, String car_type, String luggage12, String pickAddress, String dropAddress, String distance, String carId) {
        if (validationLoginCheck(img, car_type, luggage12) == true) {
            CommonMeathod.hideKeyboard(context);
            sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
            EditText et_passerger, et_carName, et_carNumber, et_preferanceType, et_carType;
            //Toast.makeText(context, car_type, Toast.LENGTH_SHORT).show();
            // Toast.makeText(context, luggage12, Toast.LENGTH_SHORT).show();

            et_passerger = view.findViewById(R.id.et_passerger);
            et_carName = view.findViewById(R.id.et_carName);
            et_carNumber = view.findViewById(R.id.et_carNumber);
            et_preferanceType = view.findViewById(R.id.et_preferanceType);
            et_carType = view.findViewById(R.id.et_carType);
            String[] splited = new String[0];
            //  Log.e("imgpath====", "" + distance);

            splited = distance.split("\\s+");
            distance = splited[0];


           /* Log.e("imgpath====", "" + l1);
            Log.e("imgpath====", "" + t1);
            Log.e("imgpath====", "" + l2);
            Log.e("imgpath====", "" + start_date);
            Log.e("imgpath====", "" + pickAddress);
            Log.e("imgpath====", "" + dropAddress);
            Log.e("imgpath====", "" + et_preferanceType.getText().toString());
            Log.e("imgpath====", "" + carId);
            Log.e("imgpath====", "" + splited[0]);*/


            //Toast.makeText(context, img, Toast.LENGTH_SHORT).show();
            File file = new File(img);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("carPic", file.getName(), requestFile);
            RequestBody serviceKey = RequestBody.create(MediaType.parse("multipart/form-data"), sharedPreferences.getString(SharedPreferenceConstants.serviceKey, ""));
            RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), sharedPreferences.getString(SharedPreferenceConstants.userId, ""));
            RequestBody pickaddress = RequestBody.create(MediaType.parse("multipart/form-data"), pickAddress);
            RequestBody picklat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(l1));
            RequestBody picklongi = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(t1));
            RequestBody dropaddress = RequestBody.create(MediaType.parse("multipart/form-data"), dropAddress);
            RequestBody droplat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(l2));
            RequestBody droplong = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(t2));
            RequestBody startDate = RequestBody.create(MediaType.parse("multipart/form-data"), start_date);
            RequestBody passengers = RequestBody.create(MediaType.parse("multipart/form-data"), et_passerger.getText().toString());
            RequestBody carType = RequestBody.create(MediaType.parse("multipart/form-data"), carId);
            RequestBody carName = RequestBody.create(MediaType.parse("multipart/form-data"), et_carName.getText().toString());
            RequestBody carNumber = RequestBody.create(MediaType.parse("multipart/form-data"), et_carNumber.getText().toString());
            RequestBody luggage = RequestBody.create(MediaType.parse("multipart/form-data"), luggage12);
            RequestBody otherPreferences = RequestBody.create(MediaType.parse("multipart/form-data"), et_preferanceType.getText().toString());
            RequestBody smoking = RequestBody.create(MediaType.parse("multipart/form-data"), smoking1);
            RequestBody dis = RequestBody.create(MediaType.parse("multipart/form-data"), distance);
            RequestBody car_id = RequestBody.create(MediaType.parse("multipart/form-data"), et_carType.getText().toString());

            FunctionHelper.showDialog(context, "Loading...");
            IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
            Call<CreatePullModelclass> signInModelclassCall = iRestInterfaces.createPoll(serviceKey
                    , userId,
                    pickaddress, picklat, picklongi, dropaddress, droplat, droplong, startDate,
                    passengers, carType, carName, carNumber, luggage, otherPreferences, smoking, dis, car_id, body);

            signInModelclassCall.enqueue(new Callback<CreatePullModelclass>() {
                @Override
                public void onResponse(Call<CreatePullModelclass> call, Response<CreatePullModelclass> response) {
                    if (response.isSuccessful()) {
                        FunctionHelper.dismissDialog();
                        int status_val = Integer.parseInt(response.body().getErrorCode());
                        if (status_val == 0) {
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                            iCreateLiftPresenter.OnLoginSuccess();

                        } else if (status_val == 1) {
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CreatePullModelclass> call, Throwable t) {
                    FunctionHelper.dismissDialog();
                    Toast.makeText(context, "something wrong", Toast.LENGTH_SHORT).show();

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

    public boolean validationLoginCheck(String img, String car_type, String luggage12) {
        boolean check = false;
        CheckBox checkbox;

        EditText et_passerger, et_carName, et_carNumber, et_preferanceType;
        final String email;
        et_passerger = view.findViewById(R.id.et_passerger);
        et_carName = view.findViewById(R.id.et_carName);
        et_carNumber = view.findViewById(R.id.et_carNumber);
        et_preferanceType = view.findViewById(R.id.et_preferanceType);
        checkbox = view.findViewById(R.id.sighupCheckbox);
        if (checkbox.isChecked()) {
            smoking1 = "1";
        } else {
            smoking1 = "0";
        }
        if (check == false) {

            if (img.equalsIgnoreCase("checkdata")) {

                Toast.makeText(context, "Please upload car photo", Toast.LENGTH_SHORT).show();

            } else if (et_passerger.getText().toString().equalsIgnoreCase("")) {

                Toast.makeText(context, "please enter number of passengers", Toast.LENGTH_SHORT).show();

            } else if (et_passerger.getText().toString().length() > 9) {

                Toast.makeText(context, "Please enter  passengers between 0 to 10", Toast.LENGTH_SHORT).show();

            } else {

                check = true;
            }
        }
        return check;
    }

}
