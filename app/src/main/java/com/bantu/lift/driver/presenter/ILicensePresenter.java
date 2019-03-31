package com.bantu.lift.driver.presenter;

public interface ILicensePresenter {
    public  void  sendRequest();
    public  void  sendbackRequest();
    public  void  sendSignUpRequest(String name,String mobile,String email,String gender,String city,String homeCity,String password,String photo,String photoid,String license);

}
