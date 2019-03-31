package com.bantu.lift.driver.constant;

import android.app.ProgressDialog;
import android.content.Context;

public class FunctionHelper {
    private static ProgressDialog dialog;
    public  static void showDialog(Context context,String msg)
    {
        dialog = new ProgressDialog(context);
        dialog.setMessage("please wait..");
        dialog.show();
    } public  static void dismissDialog()
    {
        dialog.dismiss();
    }
}
