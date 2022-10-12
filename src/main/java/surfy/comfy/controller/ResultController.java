package surfy.comfy.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
//import surfy.comfy.data.result.QuestionAnswerResponse;
//import surfy.comfy.data.result.QuestionResponse;
import surfy.comfy.data.result.SurveyResultResponse;
import surfy.comfy.data.result.RespondentsResponse;
import surfy.comfy.entity.Answer;
import surfy.comfy.entity.Question;
import surfy.comfy.service.ResultService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/resultSurvey/{survey_id}")
    public BaseResponse<SurveyResultResponse> getSurvey(@PathVariable(name="survey_id") Long surveyId){
        SurveyResultResponse surveyDetails = new SurveyResultResponse(resultService.getSurveyById(surveyId));

        return new BaseResponse<>(surveyDetails);
    }
    // 개별 보기 - 응답자 수
    @GetMapping("/resultSurvey/individual/{survey_id}")
    public BaseResponse<List<RespondentsResponse>> getSurveyIndividual(@PathVariable(name="survey_id") Long surveyId){
        List<RespondentsResponse> responseList = resultService.getRespondents(surveyId);

        return new BaseResponse<>(responseList);
    }

    // 문항별 보기 - Query(질문내용, 질문 타입, 타입에 따른 답변) - 변경해야 함 나중에
    @GetMapping("/resultSurvey/question/{survey_id}")
    public BaseResponse<List<Question>> getSurveyQuestion(@PathVariable(name="survey_id") Long surveyId){
        List<Question> questionList = resultService.getQuestionList(surveyId);

        return new BaseResponse<>(questionList);
    }
    // 개인 응답

//    @GetMapping("/resultSurvey/individual_result/{survey_id}/{user_id}")
//    public BaseResponse<QuestionAnswerResponse> getSurveyAnswer(@PathVariable(name="survey_id") Long surveyId, @PathVariable(name="user_id") Long userId){
//        List<Answer> answerList = resultService.getAnswers(surveyId, userId);
//        List<QuestionResponse> questionResponseList = resultService.getQuestionResponse(surveyId);
//
//        QuestionAnswerResponse result = new QuestionAnswerResponse();
//        result.setQuestionResponse(questionResponseList);
//        result.setAnswerList(answerList);
//        return new BaseResponse<>(result);
//    }

    @GetMapping("/resultSurvey/individual_result/{survey_id}/{user_id}")
    public BaseResponse<List<Answer>> getSurveyAnswer(@PathVariable(name="survey_id") Long surveyId, @PathVariable(name="user_id") Long userId){
        List<Answer> answerList = resultService.getAnswers(surveyId, userId);

        return new BaseResponse<>(answerList);
    }
}
