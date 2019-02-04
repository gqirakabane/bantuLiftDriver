package com.example.saurabh.driver.presenter;

public interface ICreateLiftPresenter {
    public  void  sendRequest(String img,double l1,double t1,double l2,double t2,String start_date,String car_type,String luggage,String pickAddress,String  dropAddress,String distance,String carId);
    public  void  sendForgotScreenRequest();
    public  void  sendSignUpRequest();
}
