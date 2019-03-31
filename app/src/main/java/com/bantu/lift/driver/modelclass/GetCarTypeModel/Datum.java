
package com.bantu.lift.driver.modelclass.GetCarTypeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("carTypeId")
    @Expose
    private String carTypeId;
    @SerializedName("carTypeName")
    @Expose
    private String carTypeName;

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

}
