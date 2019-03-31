
package com.bantu.lift.driver.modelclass.RequestPollModelData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pollId")
    @Expose
    private String pollId;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("carType")
    @Expose
    private String carType;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;
    @SerializedName("pickupLatitude")
    @Expose
    private String pickupLatitude;
    @SerializedName("pickupLongitude")
    @Expose
    private String pickupLongitude;
    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;
    @SerializedName("dropLatitude")
    @Expose
    private String dropLatitude;
    @SerializedName("dropLongitude")
    @Expose
    private String dropLongitude;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("carPic")
    @Expose
    private String carPic;
    @SerializedName("passengers")
    @Expose
    private String passengers;
    @SerializedName("carName")
    @Expose
    private String carName;
    @SerializedName("carNumber")
    @Expose
    private String carNumber;
    @SerializedName("luggage")
    @Expose
    private String luggage;
    @SerializedName("otherPreferences")
    @Expose
    private String otherPreferences;
    @SerializedName("smoking")
    @Expose
    private String smoking;
    @SerializedName("seats")
    @Expose
    private String seats;
    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    @SerializedName("amount")
    @Expose
    private String amount;  @SerializedName("budget")
    @Expose
    private String budget;

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getDropLatitude() {
        return dropLatitude;
    }

    public void setDropLatitude(String dropLatitude) {
        this.dropLatitude = dropLatitude;
    }

    public String getDropLongitude() {
        return dropLongitude;
    }

    public void setDropLongitude(String dropLongitude) {
        this.dropLongitude = dropLongitude;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getCarPic() {
        return carPic;
    }

    public void setCarPic(String carPic) {
        this.carPic = carPic;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getLuggage() {
        return luggage;
    }

    public void setLuggage(String luggage) {
        this.luggage = luggage;
    }

    public String getOtherPreferences() {
        return otherPreferences;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public void setOtherPreferences(String otherPreferences) {
        this.otherPreferences = otherPreferences;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
