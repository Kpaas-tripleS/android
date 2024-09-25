package com.example.android.match.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.android.R;
import com.example.android.global.RetrofitClient;
import com.example.android.global.dto.response.ResponseTemplate;
import com.example.android.match.API.MatchAPI;
import com.example.android.match.dto.response.MatchResultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchResultActivity extends AppCompatActivity {

    private MatchAPI matchAPI;
    private Long matchId;
    private ImageView player_profile;
    private ImageView opponent_profile;
    private TextView player_name;
    private TextView opponent_name;
    private FrameLayout match_win;
    private FrameLayout match_lose;
    private ImageButton return_button;
    private MatchResultResponse result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RetrofitClient retrofit = RetrofitClient.getInstance(this);

        Intent intent = getIntent();
        matchId = intent.getLongExtra("MATCH_ID", 0);
        player_profile = findViewById(R.id.match_player_profile);
        opponent_profile = findViewById(R.id.match_opponent_profile);
        player_name = findViewById(R.id.result_player_name_text);
        opponent_name = findViewById(R.id.result_opponent_name_text);
        match_win = findViewById(R.id.match_win);
        match_lose = findViewById(R.id.match_lose);
        return_button = findViewById(R.id.match_return_button);
        matchAPI = retrofit.getMatchAPI();


        Call<ResponseTemplate<MatchResultResponse>> call = matchAPI.resultQuizForMatch(matchId);
        call.enqueue(new Callback<ResponseTemplate<MatchResultResponse>>() {
            @Override
            public void onResponse(Call<ResponseTemplate<MatchResultResponse>> call, Response<ResponseTemplate<MatchResultResponse>> response) {
                if(response.isSuccessful()) {
                    result = response.body().getResults();
                    setResult(result);
                }
            }
            @Override
            public void onFailure(Call<ResponseTemplate<MatchResultResponse>> call, Throwable t) {
            }
        });

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MatchResultActivity.this, MatchMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setResult(MatchResultResponse result) {
        player_name.setText(result.getPlayer().getNickname());
        opponent_name.setText(result.getOpponent().getNickname());
        Long playerScore = result.getPlayerScore();
        Long opponentScore = result.getOpponentScore();
        String playerProfile = result.getPlayer().getProfile_image();
        String opponentProfile = result.getOpponent().getProfile_image();
        if(playerProfile != null) {
            Glide.with(this)
                    .load(playerProfile)
                    .error(R.drawable.match_default_profile)
                    .into(player_profile);
        }
        if(opponentProfile != null) {
            Glide.with(this)
                    .load(opponentProfile)
                    .error(R.drawable.match_default_profile)
                    .into(opponent_profile);
        }
        if(playerScore >= opponentScore)
            match_win.setVisibility(View.VISIBLE);
        else
            match_lose.setVisibility(View.VISIBLE);
    }

}