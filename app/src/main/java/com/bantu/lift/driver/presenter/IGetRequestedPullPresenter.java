package com.bantu.lift.driver.presenter;

public interface IGetRequestedPullPresenter {
    public  void  sendRequest();
    public  void  sendForgotScreenRequest();
    public  void  sendSignUpRequest();
    public  void  actionOnRequestedPoll(String pollId, String requestId,int postion);
}
