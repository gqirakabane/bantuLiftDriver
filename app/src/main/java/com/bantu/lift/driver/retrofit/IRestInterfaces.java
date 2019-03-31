package com.bantu.lift.driver.retrofit;

import com.bantu.lift.driver.modelclass.ActionRequestedPollModel.ActionOnRequestedPollModel;
import com.bantu.lift.driver.modelclass.CreatepullModelclass.CreatePullModelclass;
import com.bantu.lift.driver.modelclass.FcmModel.FcmModelclass;
import com.bantu.lift.driver.modelclass.ForgotModel.ForgotModelclass;
import com.bantu.lift.driver.modelclass.GetCarTypeModel.CarTypeModelclass;
import com.bantu.lift.driver.modelclass.GetPullCreatedModelclass.GetPullModelclass;
import com.bantu.lift.driver.modelclass.LogoutModelclass.LogoutModelclass;
import com.bantu.lift.driver.modelclass.NotificationModel.NotificationModelclass;
import com.bantu.lift.driver.modelclass.RequestPollModelData.RequestPollModel;
import com.bantu.lift.driver.modelclass.signmodelclass.SignInModelclass;
import com.bantu.lift.driver.modelclass.signupmodel.SignUpModelclass;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IRestInterfaces {
    @FormUrlEncoded
    @POST("login")
    Call<SignInModelclass> signInUser(@Field("emailMobile") String useremail,
                                      @Field("password") String userpassword,
                                      @Field("serviceKey") String serviceKey,
                                      @Field("deviceType") String deviceType,
                                      @Field("fcmToken") String fcmToken ,
                                      @Field("userType") String userType);

    @Multipart
    @POST("signUp")
    Call<SignUpModelclass> signupUser1(@Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part("mobile") RequestBody mobile,
                                       @Part("gender") RequestBody gender,
                                       @Part("workCity") RequestBody workCity,
                                       @Part("homeCity") RequestBody homeCity,
                                       @Part("password") RequestBody password ,
                                       @Part("serviceKey") RequestBody serviceKey ,
                                       @Part("deviceType") RequestBody deviceType ,
                                       @Part("fcmToken") RequestBody fcmToken,
                                       @Part("userType") RequestBody userType,
                                       @Part MultipartBody.Part image ,
                                       @Part MultipartBody.Part identity ,
                                       @Part MultipartBody.Part license);


    @FormUrlEncoded
    @POST("signUp")
    Call<SignUpModelclass> signupUser(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("mobile") String mobile,
                                      @Field("gender") String gender,
                                      @Field("workCity") String workCity,
                                      @Field("homeCity") String homeCity,
                                      @Field("password") String password ,
                                      @Field("serviceKey") String serviceKey ,
                                      @Field("deviceType") String deviceType ,
                                      @Field("fcmToken") String fcmToken,
                                      @Field("userType") String userType);

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ForgotModelclass> forgotPassword(@Field("email") String email);
@FormUrlEncoded
    @POST("logout")
    Call<LogoutModelclass> logoutUser(@Field("serviceKey") String serviceKey,
                                      @Field("userId") String userId);
    @Multipart
    @POST("createPoll")
    Call<CreatePullModelclass> createPoll(@Part("serviceKey") RequestBody serviceKey,
                                          @Part("userId") RequestBody userId,
                                          @Part("pickupAddress") RequestBody pickupAddress,
                                          @Part("pickupLatitude") RequestBody pickupLatitude,
                                          @Part("pickupLongitude") RequestBody pickupLongitude,
                                          @Part("dropAddress") RequestBody dropAddress,
                                          @Part("dropLatitude") RequestBody dropLatitude ,
                                          @Part("dropLongitude") RequestBody dropLongitude ,
                                          @Part("startDateTime") RequestBody startDateTime ,
                                          @Part("passengers") RequestBody passengers,
                                          @Part("carType") RequestBody carType,
                                          @Part("carName") RequestBody carName,
                                          @Part("carNumber") RequestBody carNumber,
                                          @Part("luggage") RequestBody luggage,
                                          @Part("otherPreferences") RequestBody otherPreferences,
                                          @Part("smoking") RequestBody smoking,
                                          @Part("distance") RequestBody distance,
                                          @Part("carTypeText") RequestBody carTypeText,
                                          @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("getMyPolls")
    Call<GetPullModelclass> getMyPolls(@Field("serviceKey") String serviceKey,
                                       @Field("userId") String userId);
 @FormUrlEncoded
    @POST("getAllCarTypes")
    Call<CarTypeModelclass> getAllCarTypes(@Field("serviceKey") String serviceKey,
                                           @Field("userId") String userId);
    @FormUrlEncoded
    @POST("getRequestedPolls")
    Call<RequestPollModel> getRequestedPolls(@Field("serviceKey") String serviceKey,
                                             @Field("userId") String userId);
    @FormUrlEncoded
    @POST("actionOnRequestedPoll")
    Call<ActionOnRequestedPollModel> actionOnRequestedPoll(@Field("serviceKey") String serviceKey,
                                                           @Field("userId") String userId,
                                                           @Field("requestId") String requestId,
                                                           @Field("status") String status);
    @FormUrlEncoded
    @POST("getNotifications")
    Call<NotificationModelclass> getNotifications(@Field("serviceKey") String serviceKey,
                                                  @Field("userId") String userId);
@FormUrlEncoded
    @POST("updateToken")
    Call<FcmModelclass> updateToken(@Field("fcmToken") String fcmToken,
                                    @Field("userId") String userId);

}
