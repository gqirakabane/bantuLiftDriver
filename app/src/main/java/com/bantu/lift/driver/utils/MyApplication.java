package com.bantu.lift.driver.utils;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.AcraMailSender;


@AcraMailSender( //will not be used
        mailTo = "thekundankamal@gmail.com")
    public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        mInstance = this;
    }





}
