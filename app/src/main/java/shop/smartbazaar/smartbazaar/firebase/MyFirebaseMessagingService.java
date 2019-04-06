package shop.smartbazaar.smartbazaar.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.MainActivity;


    public class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = "MyFirebaseMsgService";

        /**
         * Called when message is received.
         *
         * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
         */
        // [START receive_message]
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {


            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//                if (/* Check if data needs to be processed by long running job */ true) {
//                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                    scheduleJob();
//                } else {
//                    // Handle message within 10 seconds
//                    handleNow();
//                }

            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {

                createNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            }


        }

        /**
         * Called if InstanceID token is updated. This may occur if the security of
         * the previous token had been compromised. Note that this is called when the InstanceID token
         * is initially generated so this is where you would retrieve the token.
         */
        @Override
        public void onNewToken(String token) {
            Log.d(TAG, "Refreshed token: " + token);

            sendRegistrationToServer(token);
        }
        // [END on_new_token]

        /**
         * Schedule a job using FirebaseJobDispatcher.
         */
        private void scheduleJob() {
            // [START dispatch_job]
//            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//            Job myJob = dispatcher.newJobBuilder()
//                    .setService(MyJobService.class)
//                    .setTag("my-job-tag")
//                    .build();
//            dispatcher.schedule(myJob);
            // [END dispatch_job]
        }

        /**
         * Handle time allotted to BroadcastReceivers.
         */
        private void handleNow() {
            Log.d(TAG, "Short lived task is done.");
        }

        /**
         * Persist token to third-party servers.
         *
         * Modify this method to associate the user's FCM InstanceID token with any server-side account
         * maintained by your application.
         *
         * @param token The new token.
         */
        private void sendRegistrationToServer(String token) {
            // TODO: Implement this method to send token to your app server.
        }

        /**
         * Create and show a simple notification containing the received FCM message.
         *
         * @param messageBody FCM message body received.
         */
        private void sendNotification(String messageBody) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = "sajjad";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }



        public void createNotification(String title, String body){
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel.
                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            }


            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setTicker("Hearty365")
                    //     .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("Info");

            notificationManager.notify(/*notification id*/1, notificationBuilder.build());

        }
    }