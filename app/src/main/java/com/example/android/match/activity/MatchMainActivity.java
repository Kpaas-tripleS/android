package com.example.android.match.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android.R;
import com.example.android.global.dto.response.ResponseTemplate;
import com.example.android.match.API.MatchAPI;
import com.example.android.match.API.RankingAPI;
import com.example.android.match.dto.response.MatchResponse;
import com.example.android.match.dto.response.MatchResponseList;
import com.example.android.match.dto.response.RankingResponseList;
import com.example.android.match.dto.response.UserResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MatchMainActivity extends AppCompatActivity {

    private int currentIndex = 0;
    private List<MatchResponse> matchList;
    private TextView match_list_name;
    private TextView ranking_text;
    private ImageButton right_button;
    private ImageButton left_button;
    private ImageButton all_rank_button;
    private ImageButton friend_rank_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjY1NTgzNjQsImV4cCI6MTcyNjU2MTk2NCwiaXNzIjoidHJpcGxlcyIsInN1YiI6IjEiLCJyb2xlIjoiQURNSU4ifQ.z-5P9h0otvOiq76RLCxIdgpjDbdpLFHJ_hWctmeiSPigJ2VNflnkpH0nvbSySv6OzlI4vNBctnqoUrEli03hgg")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


        match_list_name = findViewById(R.id.match_list_name);
        MatchAPI matchAPI = retrofit.create(MatchAPI.class);
        Call<ResponseTemplate<MatchResponseList>> call = matchAPI.getMatchList();
        call.enqueue(new Callback<ResponseTemplate<MatchResponseList>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<MatchResponseList>> call, Response<ResponseTemplate<MatchResponseList>> response) {
                if (response.isSuccessful()) {
                    MatchResponseList results = response.body().getResults();
                    matchList = results.getMatchResponseList();
                    if (matchList != null) {
                        MatchResponse match = matchList.get(currentIndex);
                        match_list_name.setText(match.getLeader().getNickname());
                    } else {
                        match_list_name.setText(""); //나중에 이미지넣기
                    }
                } else {
                    Log.e("MatchMainActivity", "Response failed: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseTemplate<MatchResponseList>> call, Throwable t) {
                Log.e("MatchMainActivity", "API call failed: " + t.getMessage());
            }
        });
        right_button = findViewById(R.id.match_right_button);
        right_button.setOnClickListener(v -> {
            if (matchList != null && currentIndex < matchList.size() - 1) {
                currentIndex++;
                MatchResponse match = matchList.get(currentIndex);
                match_list_name.setText(match.getLeader().getNickname());
            }
            else if(matchList != null && currentIndex == (matchList.size() - 1)) {
                currentIndex = 0;
                MatchResponse match = matchList.get(currentIndex);
                match_list_name.setText(match.getLeader().getNickname());
            }
        });
        left_button = findViewById(R.id.match_left_button);
        left_button.setOnClickListener(v -> {
            if (matchList != null) {
                if (currentIndex - 1 < 0) currentIndex = (matchList.size() - 1);
                else currentIndex--;
                MatchResponse match = matchList.get(currentIndex);
                match_list_name.setText(match.getLeader().getNickname());
            }
        });


        ranking_text = findViewById(R.id.ranking_text);
        all_rank_button = findViewById(R.id.all_rank);
        all_rank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RankingAPI rankingAPI = retrofit.create(RankingAPI.class);
                Call<ResponseTemplate<RankingResponseList>> call = rankingAPI.getAllRanking();
                call.enqueue(new Callback<ResponseTemplate<RankingResponseList>>() {
                    @Override
                    public void onResponse(Call<ResponseTemplate<RankingResponseList>> call, Response<ResponseTemplate<RankingResponseList>> response) {
                        if(response.isSuccessful()) {
                            RankingResponseList results = response.body().getResults();
                            List<UserResponse> allRanking = results.getRankingResponseList();
                            int i = 1;
                            for(UserResponse ranking : allRanking) {
                                String content = i + "  " + ranking.getNickname() + "\n";
                                ranking_text.append(content);
                                i++;
                            }
                        }
                        else {
                            ranking_text.setText("Code: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseTemplate<RankingResponseList>> call, Throwable t) {
                        ranking_text.setText(t.getMessage());
                        Log.getStackTraceString(t);
                    }
                });
            }
        });
        friend_rank_button = findViewById(R.id.friend__rank);
        friend_rank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RankingAPI rankingAPI = retrofit.create(RankingAPI.class);
                Call<ResponseTemplate<RankingResponseList>> call = rankingAPI.getFriendRanking();
                call.enqueue(new Callback<ResponseTemplate<RankingResponseList>>() {
                    @Override
                    public void onResponse(Call<ResponseTemplate<RankingResponseList>> call, Response<ResponseTemplate<RankingResponseList>> response) {
                        if(response.isSuccessful()) {
                            RankingResponseList results = response.body().getResults();
                            List<UserResponse> friendRanking = results.getRankingResponseList();
                            int i = 1;
                            for(UserResponse ranking : friendRanking) {
                                String content = i + " " + ranking.getNickname() + "\n";
                                ranking_text.append(content);
                                i++;
                            }
                        }
                        else {
                            ranking_text.setText("Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseTemplate<RankingResponseList>> call, Throwable t) {
                        ranking_text.setText(t.getMessage());
                        Log.getStackTraceString(t);
                    }
                });
            }
        });
        all_rank_button.performClick();


    }
}