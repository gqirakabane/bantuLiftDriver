
package com.bantu.lift.driver.modelclass.FcmModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FcmModelclass {

    @SerializedName("errorCode")
    @Expose
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
