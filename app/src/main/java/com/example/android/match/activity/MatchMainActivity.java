package com.example.android.match.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android.R;
import com.example.android.global.dto.response.ResponseTemplate;
import com.example.android.match.API.MatchAPI;
import com.example.android.match.API.RankingAPI;
import com.example.android.match.dto.request.MatchRequest;
import com.example.android.match.dto.response.MatchResponse;
import com.example.android.match.dto.response.MatchResponseList;
import com.example.android.match.dto.response.MatchStartResponse;
import com.example.android.match.dto.response.RankingResponseList;
import com.example.android.match.dto.response.UserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.dto.StompHeader;

public class MatchMainActivity extends AppCompatActivity {

    private final String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjY5NDA1OTQsImV4cCI6MTcyNjk0NDE5NCwiaXNzIjoidHJpcGxlcyIsInN1YiI6IjEiLCJyb2xlIjoiQURNSU4ifQ.Q-rUrRLBPyOQF-k3TTXKZno18vM9RLIj8xEzierwh2dhvsSeGXlbMGjvlFx76bWs1RORhpIXLEvbpSmE5sfmfw";
    private ua.naiksoftware.stomp.StompClient stompClient;
    private int currentIndex;
    private List<MatchResponse> matchList;
    private Long matchId;
    private Long friendMatchId;
    private String url;
    private MatchAPI matchAPI;
    private RankingAPI rankingAPI;
    private TextView match_list_name;
    private TextView ranking_text;
    private ImageButton random_match_button;
    private ImageButton right_button;
    private ImageButton left_button;
    private ImageButton all_rank_button;
    private ImageButton friend_rank_button;
    private ImageButton match_delete;
    private ImageButton match_accept;
    private ImageButton match_reject;
    private ImageButton check_accept;
    private ImageButton check_reject;
    private FrameLayout check_toast;
    private FrameLayout match_fail_toast;
    private RelativeLayout match_random_loading;
    private boolean friendMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OkHttpClient okHttpClient = new OkHttpClient.Builder() //임시 토큰
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", token)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        matchAPI = retrofit.create(MatchAPI.class);
        rankingAPI = retrofit.create(RankingAPI.class);
        right_button = findViewById(R.id.match_right_button);
        left_button = findViewById(R.id.match_left_button);
        match_accept = findViewById(R.id.match_accept_button);
        match_reject = findViewById(R.id.match_reject_button);
        check_accept = findViewById(R.id.match_check_accept);
        check_reject = findViewById(R.id.match_check_reject);
        check_toast = findViewById(R.id.match_check_toast);
        random_match_button = findViewById(R.id.random_match_button);
        match_random_loading = findViewById(R.id.match_random_loading);
        match_list_name = findViewById(R.id.match_list_name);
        ranking_text = findViewById(R.id.ranking_text);
        all_rank_button = findViewById(R.id.all_rank);
        friend_rank_button = findViewById(R.id.friend__rank);
        match_fail_toast = findViewById(R.id.match_fail_toast);
        match_delete = findViewById(R.id.match_delete);
        currentIndex = 0;


        random_match_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String formattedDateTime = sdf.format(calendar.getTime());
                MatchRequest matchRequest = new MatchRequest(formattedDateTime);
                Call<Long> call = matchAPI.findMatch(matchRequest);
                call.enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                     if(response.isSuccessful()) {
                         matchId = response.body();
                         url = "/topic/matches/" + matchId;
                         match_random_loading.setVisibility(View.VISIBLE);
                         startStomp();
                     }
                    }
                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        Log.e("MatchMainActivity", "API call failed: " + t.getMessage());
                    }
                });
            }
        });

        match_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseTemplate<Void>> call = matchAPI.deleteMatch(matchId);
                call.enqueue(new Callback<ResponseTemplate<Void>>() {
                    @Override
                    public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {
                        if(response.isSuccessful()) {
                            match_random_loading.setVisibility(View.GONE);
                            stompClient.disconnect();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {

                    }
                });
            }
        });

        Call<ResponseTemplate<MatchResponseList>> call = matchAPI.getMatchList();
        call.enqueue(new Callback<ResponseTemplate<MatchResponseList>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<MatchResponseList>> call, Response<ResponseTemplate<MatchResponseList>> response) {
                if (response.isSuccessful()) {
                    MatchResponseList results = response.body().getResults();
                    matchList = results.getMatchResponseList();
                    if (matchList != null) {
                        try {
                            MatchResponse match = matchList.get(currentIndex);
                            match_list_name.setText(match.getLeader().getNickname());
                            friendMatchId = match.getMatchId();
                        }
                        catch (Exception e) {
                            match_list_name.setText(""); //나중에 이름 옆에 이미지 넣기 - 디자인
                        }
                    } else {
                        match_list_name.setText(""); //나중에 이름 옆에 이미지 넣기 - 디자인
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
        right_button.setOnClickListener(v -> {
            if (matchList != null && currentIndex < matchList.size() - 1) {
                currentIndex++;
                MatchResponse match = matchList.get(currentIndex);
                match_list_name.setText(match.getLeader().getNickname());
                friendMatchId = match.getMatchId();
            }
            else if(matchList != null && currentIndex == (matchList.size() - 1)) {
                currentIndex = 0;
                MatchResponse match = matchList.get(currentIndex);
                match_list_name.setText(match.getLeader().getNickname());
                friendMatchId = match.getMatchId();
            }
        });
        left_button.setOnClickListener(v -> {
            if (matchList != null) {
                if (currentIndex - 1 < 0) currentIndex = (matchList.size() - 1);
                else currentIndex--;
                MatchResponse match = matchList.get(currentIndex);
                match_list_name.setText(match.getLeader().getNickname());
                friendMatchId = match.getMatchId();
            }
        });

        match_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendMatch = true;
                check_toast.setVisibility(View.VISIBLE);
            }
        });
        match_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendMatch = false;
                check_toast.setVisibility(View.VISIBLE);
            }
        });

        check_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(friendMatch) {
                    url = "/topic/matches/" + friendMatchId;
                    startStomp();
                    Call<ResponseTemplate<Void>> call = matchAPI.acceptMatch(friendMatchId);
                    call.enqueue(new Callback<ResponseTemplate<Void>>() {
                        @Override
                        public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {
                        }
                        @Override
                        public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {
                        }
                    });
                }
                else {
                    check_toast.setVisibility(View.GONE);
                    Call<ResponseTemplate<Void>> call = matchAPI.rejectMatch(friendMatchId);
                    call.enqueue(new Callback<ResponseTemplate<Void>>() {
                        @Override
                        public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {
                        }
                        @Override
                        public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {
                        }
                    });
                }
            }
        });
        check_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_toast.setVisibility(View.GONE);
            }
        });

        all_rank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ranking_text.setText("");
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
        friend_rank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ranking_text.setText("");
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

    private void startStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws");
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", token));
        stompClient.connect(headers);
        //stompClient.connect();
        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            System.out.println("WebSocket 연결 성공");
                            subscribeToTopic();
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

    private void subscribeToTopic() {
        stompClient.topic(url).subscribe(topicMessage -> {
            handleStomp(topicMessage.getPayload());
        }, throwable -> {
            // onError handler
            Log.e("WebSocket", "Error sending message: " + throwable.getMessage());
        });
    }

    private void handleStomp(String message) {
        if(message.equals("MATCH_FAIL")) {
            handleMatchFail();
        }
        else {
            try {
                Gson gson = new GsonBuilder().create();
                MatchStartResponse result = gson.fromJson(message, MatchStartResponse.class);
                handleMatchComplete(matchId, result);
            } catch (JsonSyntaxException e) {
                System.err.println("메시지 변환 오류: " + e.getMessage());
            }
        }
    }

    private void handleMatchComplete(Long matchId,MatchStartResponse result) {
        Intent intent = new Intent(this, MatchActivity.class);
        if(friendMatch)
            intent.putExtra("MATCH_ID", friendMatchId);
        else
            intent.putExtra("MATCH_ID", matchId);
        intent.putExtra("MATCH_START_RESPONSE", result);
        stompClient.disconnect();
        startActivity(intent);
    }

    private void handleMatchFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                match_random_loading.setVisibility(View.GONE);
                match_fail_toast.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        match_fail_toast.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });
        stompClient.disconnect();
    }

}