
package com.example.saurabh.driver.modelclass.ActionRequestedPollModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionOnRequestedPollModel {

    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errorMsg")
    @Expose
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
