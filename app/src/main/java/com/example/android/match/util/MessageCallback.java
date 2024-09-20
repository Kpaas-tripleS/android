package com.example.android.match.util;

public interface MessageCallback {
 void onMessageReceived(Object message);
 void onError(Throwable throwable);
}

