package com.example.android.match.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android.R;
import com.example.android.global.RetrofitClient;
import com.example.android.global.dto.response.ResponseTemplate;
import com.example.android.match.API.MatchAPI;
import com.example.android.match.dto.QuizDto;
import com.example.android.match.dto.response.MatchStartResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.dto.StompHeader;

public class MatchActivity extends AppCompatActivity {

    private String token;
    private ua.naiksoftware.stomp.StompClient stompClient;
    private ViewGroup rootView;
    private Long matchId;
    private String url;
    private boolean clickAnswer;
    private MatchAPI matchAPI;
    private int quizNumber;
    private List<QuizDto> quizzes;
    private MatchStartResponse matchInfo;
    private TextView player_name_text;
    private TextView opponent_name_text;
    private TextView quiz_text;
    private FrameLayout answer_container_one;
    private FrameLayout answer_container_two;
    private FrameLayout answer_container_three;
    private FrameLayout answer_container_four;
    private TextView answer_one;
    private TextView answer_two;
    private TextView answer_three;
    private TextView answer_four;
    private RelativeLayout wait_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rootView = findViewById(R.id.main);

        RetrofitClient retrofit = RetrofitClient.getInstance(this);
        token = retrofit.getAccessToken(this);

        Intent intent = getIntent();
        matchInfo = intent.getParcelableExtra("MATCH_START_RESPONSE");
        matchId = intent.getLongExtra("MATCH_ID", 0);
        url = "/topic/matches/" + matchId;
        startStomp();
        matchAPI = retrofit.getMatchAPI();

        player_name_text = findViewById(R.id.player_name_text);
        opponent_name_text = findViewById(R.id.opponent_name_text);
        quiz_text = findViewById(R.id.quiz_text);
        answer_container_one = findViewById(R.id.answer_container_one);
        answer_container_two = findViewById(R.id.answer_container_two);
        answer_container_three = findViewById(R.id.answer_container_three);
        answer_container_four = findViewById(R.id.answer_container_four);
        answer_one = findViewById(R.id.quiz_answer_one);
        answer_two = findViewById(R.id.quiz_answer_two);
        answer_three = findViewById(R.id.quiz_answer_three);
        answer_four = findViewById(R.id.quiz_answer_four);
        wait_toast = findViewById(R.id.wait_toast);


        player_name_text.setText(matchInfo.getPlayer().getNickname());
        opponent_name_text.setText(matchInfo.getOpponent().getNickname());

        quizzes = matchInfo.getQuizzes();
        quizNumber = 0;
        showQuiz();

        answer_container_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnswer = true;
                Call<ResponseTemplate<Void>> call = matchAPI.checkQuizForMatch(matchId, quizzes.get(quizNumber).getQuizId(), quizzes.get(quizNumber).getChoiceOne());
                call.enqueue(new Callback<ResponseTemplate<Void>>() {
                    @Override
                    public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {

                    }
                });
            }
        });
        answer_container_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnswer = true;
                Call<ResponseTemplate<Void>> call = matchAPI.checkQuizForMatch(matchId, quizzes.get(quizNumber).getQuizId(), quizzes.get(quizNumber).getChoiceTwo());
                call.enqueue(new Callback<ResponseTemplate<Void>>() {
                    @Override
                    public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {

                    }
                });
            }
        });
        answer_container_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnswer = true;
                Call<ResponseTemplate<Void>> call = matchAPI.checkQuizForMatch(matchId, quizzes.get(quizNumber).getQuizId(), quizzes.get(quizNumber).getChoiceThree());
                call.enqueue(new Callback<ResponseTemplate<Void>>() {
                    @Override
                    public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {

                    }
                });
            }
        });
        answer_container_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnswer = true;
                Call<ResponseTemplate<Void>> call = matchAPI.checkQuizForMatch(matchId, quizzes.get(quizNumber).getQuizId(), quizzes.get(quizNumber).getChoiceFour());
                call.enqueue(new Callback<ResponseTemplate<Void>>() {
                    @Override
                    public void onResponse(Call<ResponseTemplate<Void>> call, Response<ResponseTemplate<Void>> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseTemplate<Void>> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void startStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws");
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", "Bearer " + token));
        stompClient.connect(headers);
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
        if(message.equals("QUIZ_RIGHT")) {
            clickAnswer = false;
            if(quizNumber == (quizzes.size() - 1))
                finishQuiz();
            else
                handleQuizRight();
        }
        else if(message.equals("QUIZ_WRONG") && clickAnswer) {
            clickAnswer = false;
            handleQuizWrong();
        }
    }

    private void showQuiz() {
        QuizDto quiz = quizzes.get(quizNumber);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                quiz_text.setText(quiz.getQuestion());
                answer_one.setText(quiz.getChoiceOne());
                answer_two.setText(quiz.getChoiceTwo());
                answer_three.setText(quiz.getChoiceThree());
                answer_four.setText(quiz.getChoiceFour());
            }
        });
    }

    private void handleQuizRight() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                quizNumber++;
                showQuiz();
            }
        });
    }

    private void handleQuizWrong() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wait_toast.setVisibility(View.VISIBLE);
                rootView.setEnabled(false);
                new Handler().postDelayed(() -> {
                    wait_toast.setVisibility(View.GONE);
                    rootView.setEnabled(true);
                }, 2000);
            }
        });
    }

    private void finishQuiz() {
        Intent intent = new Intent(this, MatchResultActivity.class);
        intent.putExtra("MATCH_ID", matchId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        stompClient.disconnect();
        startActivity(intent);
        finish();
    }
}