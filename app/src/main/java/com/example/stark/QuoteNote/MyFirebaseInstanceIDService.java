package com.example.stark.QuoteNote;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        //registration token
        final String registrationToken = FirebaseInstanceId.getInstance().getToken();

        //Send to the server to register and identify the entity(mobile devices,browser front-end apps) in order to send a push notification to this entity(mobile devices,browser front-end apps).

    }
}