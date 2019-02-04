package com.example.saurabh.driver.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.saurabh.driver.MainActivity;
import com.example.saurabh.driver.R;
import com.example.saurabh.driver.activity.PushNotificationActivity;
import com.example.saurabh.driver.app.Config;
import com.example.saurabh.driver.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by iws-036 on 5/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a notification payload.
        //if (remoteMessage.getNotification() != null) {
           // Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
           // Log.e(TAG, "Notification Body1: " + remoteMessage.getNotification().getTitle());
            Log.e(TAG, "Notification Body2: " + remoteMessage.getData());
           // Log.e(TAG, "Notification Body3: " + remoteMessage.getNotification());
          //  Log.e(TAG, "Notification Body4: " + remoteMessage.getMessageId());
          //  Log.e(TAG, "Notification Body5: " + remoteMessage.getFrom());
           // Log.e(TAG, "Notification Body6: " + remoteMessage.getMessageType());
           // handleNotification(remoteMessage);
       // }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody());
        builder.setAutoCancel(true);
        Intent notificationIntent = new Intent(this, PushNotificationActivity.class);
        notificationIntent.putExtra("message", remoteMessage.getData().get("id"));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        // Check if message contains a data payload.
        /*if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }*/

       // startService(new Intent(this, MyFirebaseInstanceIDService.class));
          }

    private void handleNotification(RemoteMessage message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(getApplicationContext(), PushNotificationActivity.class);
            pushNotification.putExtra("message", message.getData().get("sound"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            showNotificationMessage(getApplicationContext(), message.getData().get("title"), message.getData().get("body"),
                    "", pushNotification);
            Log.e("getdatashow=",message.getData().get("title"));

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");



            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);



            if(!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
/* extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "getTo: " + remoteMessage.getTo());
        Log.e(TAG, "getCollapseKey: " + remoteMessage.getCollapseKey());
        Log.e(TAG, "getFrom: " + remoteMessage.getFrom());
        Log.e(TAG, "getMessageId: " + remoteMessage.getMessageId());
        Log.e(TAG, "getMessageType: " + remoteMessage.getMessageType());
        Log.e(TAG, "getData: " + remoteMessage.getData());
        Log.e(TAG, "getBody: " + remoteMessage.getNotification().getBody());
        Log.e(TAG, "getTitle: " + remoteMessage.getNotification().getTitle());
        Log.e(TAG, "getIcon: " + remoteMessage.getNotification().getIcon());
        Log.e(TAG, "getSound: " + remoteMessage.getNotification().getSound());
        //Log.e(TAG, "Notification Message Body: " + getIntent.getNotification());

//        setNotification(remoteMessage.getNotification().getBody());


    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        Log.e(TAG, "From: " + intent.getExtras().getString("data","no null"));
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_flag_icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationFragment.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationFragment.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
        mNotificationManager.notify(0, mBuilder.build());
    }
}*/