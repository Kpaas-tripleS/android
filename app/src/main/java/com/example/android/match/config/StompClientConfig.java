package com.example.android.match.config;

import com.example.android.match.dto.response.MatchStartResponse;
import com.example.android.match.util.MessageCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;

public class StompClientConfig {

    private ua.naiksoftware.stomp.StompClient stompClient;
    private MessageCallback messageCallback;

    public void setMessageCallback(MessageCallback callback) {
        this.messageCallback = callback;
    }

    public void connectStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws");
        stompClient.connect();

        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            System.out.println("WebSocket 연결 성공");
                            break;
                        case ERROR:
                            System.err.println("WebSocket 오류: " + lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            System.out.println("WebSocket 연결 종료");
                            break;
                    }
                });
    }

    // 메시지 구독
    /*
    public void subscribe(String topic) {
        stompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    String payload = stompMessage.getPayload();
                    try {
                        System.out.println("Received payload: " + payload);
                        if(payload.equals("MATCH_FAIL") || payload.equals("MATCH_REJECT") || payload.equals("QUIZ_RIGHT") || payload.equals("QUIZ_WRONG"))
                            messageCallback.onMessageReceived(payload);
                        else if(payload.startsWith("{") && payload.endsWith("}")) {
                            Gson gson = new GsonBuilder().create();
                            MatchStartResponse response = gson.fromJson(payload, MatchStartResponse.class);
                            messageCallback.onMessageReceived(response);
                        }
                        else
                            System.err.println("Unexpected message format: " + payload);
                    } catch (Exception e) {
                        System.err.println("메시지 처리 오류: " + e.getMessage());
                    }
                }, throwable -> {
                    if (messageCallback != null) {
                        messageCallback.onError(throwable);
                    }
                    //System.err.println("구독 오류: " + throwable.getMessage());
                });
    }*/

    // 메시지 전송
    public void sendMessage(String destination, String message) {
        stompClient.send(destination, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    System.out.println("메시지 전송 성공");
                }, throwable -> {
                    System.err.println("메시지 전송 오류: " + throwable.getMessage());
                });
    }

    // WebSocket 종료
    public void disconnectStomp() {
        stompClient.disconnect();
    }
}

