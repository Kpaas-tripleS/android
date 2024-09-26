package com.example.android.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.android.friend.api.BeFriendApi;
import com.example.android.friend.api.FriendApi;
import com.example.android.match.API.MatchAPI;
import com.example.android.match.API.RankingAPI;
import com.example.android.user.api.UserApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static RetrofitClient instance;
    private final UserApi userApi;
    private final BeFriendApi befriendApi;
    private final FriendApi friendApi;
    private final MatchAPI matchAPI;
    private final RankingAPI rankingAPI;
    private final String BASE_URL = "http://3.39.123.38:8081"; //http://10.0.2.2:8080/

    private RetrofitClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    String accessToken = sharedPreferences.getString("accessToken", null);
                    Log.d("AccessTokenCheck", "Access Token: " + accessToken); // 추가된 로그

                    Request.Builder requestBuilder = chain.request().newBuilder();
                    if (accessToken != null) {
                        requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
                        Log.d("access token", accessToken);
                    } else {
                        Log.d("access token", "Access token is null");
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);
        befriendApi = retrofit.create(BeFriendApi.class);
        friendApi = retrofit.create(FriendApi.class);
        matchAPI = retrofit.create(MatchAPI.class);
        rankingAPI = retrofit.create(RankingAPI.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    public String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", null);
    }

    public Long getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE); // 수정된 부분
        return sharedPreferences.getLong("userId", -1);
    }

    // Api 모음
    public UserApi getUserApi() {
        return userApi;
    }

    public BeFriendApi getBefriendApi() {
        return befriendApi;
    }

    public FriendApi getFriendApi() {
        return friendApi;
    }

    public MatchAPI getMatchAPI() {
        return matchAPI;
    }

    public RankingAPI getRankingAPI() {
        return rankingAPI;
    }

    public UserApi getFindUser() {
        return userApi;
    }
}
