package com.example.saurabh.driver.retrofit;



public class ApiUtils {

    private ApiUtils() {}
    private static final String DEV_URL="http://carpollservice.teachinghub.in/api/v1/";
    /*
    * BaseUrl of this application
    * */
    public static final String BASE_URL = "http://cmsbox.in/app/nudo/";

    public static IRestInterfaces getAPIService() {

        return RetrofitClient.getClient(DEV_URL).create(IRestInterfaces.class);
    }
}
