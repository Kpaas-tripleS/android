package com.example.android.global;

import android.app.Application;
import android.content.Context;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static void kakaoSdkInit(Context context) {
        KakaoSdk.init(context, "f4360ed6d5169b705e8dbe21b0695ace");

    }
}
