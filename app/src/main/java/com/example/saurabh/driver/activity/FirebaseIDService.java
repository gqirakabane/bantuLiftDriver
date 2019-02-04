package com.example.saurabh.driver.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.saurabh.driver.utils.SharedPreferenceConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by iws-036 on 5/12/17.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCM";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
      sharedPreferences.edit().putString(SharedPreferenceConstants.fcm_reg_id, refreshedToken).apply();
      // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}