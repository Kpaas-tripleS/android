package com.example.android.ui.global;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, "f4360ed6d5169b705e8dbe21b0695ace");
    }
}
