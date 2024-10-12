package com.example.android.quiz.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.quiz.api.QuizApi;
import com.example.android.quiz.dto.QuizDto;
import com.example.android.quiz.dto.QuizAnswerDto;
import com.example.android.quiz.dto.QuizResultDto;
import com.example.android.global.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizViewModel extends ViewModel {
    private MutableLiveData<List<QuizDto>> quizzes = new MutableLiveData<>();
    private MutableLiveData<QuizResultDto> quizResult = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<QuizDto>> getQuizzes() {
        return quizzes;
    }

    public LiveData<QuizResultDto> getQuizResult() {
        return quizResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadQuizzes() {
        // 실제 네트워크 요청 대신 더미 데이터를 사용
        List<QuizDto> dummyQuizzes = createDummyQuizzes();
        quizzes.setValue(dummyQuizzes);
    }

    private List<QuizDto> createDummyQuizzes() {
        List<QuizDto> dummyQuizzes = new ArrayList<>();

        // 문제 1
        QuizDto quiz1 = new QuizDto();
        quiz1.setQuizId(1L);
        quiz1.setQuestion("다음 문장에서 밑줄 친 단어의 뜻으로 알맞은 것을 고르시오(하)\n체험학급 시 '중식'이 제공될 예정입니다.");
        quiz1.setChoiceOne("중국 음식");
        quiz1.setChoiceTwo("간식");
        quiz1.setChoiceThree("점심 식사");
        quiz1.setChoiceFour("저녁 식사");
        dummyQuizzes.add(quiz1);

        // 문제 2
        QuizDto quiz2 = new QuizDto();
        quiz2.setQuizId(2L);
        quiz2.setQuestion("다음 문장에서 밑줄 친 단어의 뜻으로 알맞은 것을 고르시오(하)\n'금일' 회의는 오후 2시에 시작됩니다.");
        quiz2.setChoiceOne("금요일");
        quiz2.setChoiceTwo("오늘");
        quiz2.setChoiceThree("내일");
        quiz2.setChoiceFour("이번 주");
        dummyQuizzes.add(quiz2);

        // 문제 3
        QuizDto quiz3 = new QuizDto();
        quiz3.setQuizId(3L);
        quiz3.setQuestion("다음 문장에서 빈칸에 들어갈 가장 적절한 단어를 고르시오(중)\n\"친구는 여러 번의 실패에도 불구하고 포기하지 않았고, ___ 끝에 마침내 목표를 이뤘다\"");
        quiz3.setChoiceOne("노력");
        quiz3.setChoiceTwo("좌절");
        quiz3.setChoiceThree("도전");
        quiz3.setChoiceFour("절망");
        dummyQuizzes.add(quiz3);

        // 추가 문제들...

        return dummyQuizzes;
    }

    public void submitAnswer(Long quizId, String answer) {
        // 실제 네트워크 요청 대신 더미 결과를 반환
        QuizResultDto result = new QuizResultDto();
        result.setIsCorrect(true); // 또는 false
        quizResult.setValue(result);
    }
}