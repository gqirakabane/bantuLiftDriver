package com.bantu.lift.driver.retrofit;



public class ApiUtils {

    private ApiUtils() {}
    private static final String DEV_URL="http://api.bantub2b.co.za/api/v1/";
    /*
    * BaseUrl of this application
    * */
    public static final String BASE_URL = "http://cmsbox.in/app/nudo/";

    public static IRestInterfaces getAPIService() {

        return RetrofitClient.getClient(DEV_URL).create(IRestInterfaces.class);
    }
}
