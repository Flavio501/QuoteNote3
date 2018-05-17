package com.example.stark.ommbc;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /*Map<String, String> data = remoteMessage.getData();
        String value1 = data.get("myKey1");
        String value2 = data.get("myKey2");
        */

    }
}