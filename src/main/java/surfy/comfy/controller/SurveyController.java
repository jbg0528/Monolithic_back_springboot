package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.manage.DeleteSurveyResponse;
import surfy.comfy.data.manage.FinishSurveyResponse;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.data.survey.GetSurveyResponse;
import surfy.comfy.data.survey.PostSurveyResponse;
import surfy.comfy.entity.Survey;
import surfy.comfy.service.SurveyService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final Logger logger= LoggerFactory.getLogger(SurveyController.class);

    /**
     * minseo
     * 마음에 드는 설문지 임시 저장
     * @param surveyId
     * @param memberId
     * @return
     */
    @PostMapping("/created-survey/{surveyId}/{memberId}")
    public BaseResponse<PostSurveyResponse> postCreatedSurvey(@PathVariable(name="surveyId")Long surveyId,@PathVariable(name="memberId")Long memberId){
        logger.info("survey controller - postCreatedSurvey");
        PostSurveyResponse response=surveyService.makeSurvey(surveyId,memberId);

        return new BaseResponse<>(response);
    }


    @GetMapping("/surveyPage/{memberId}")
    public BaseResponse<List<SurveyResponse>> getSurvey(@PathVariable(name = "memberId") Long memberId){
        logger.info("[Survey Controller] - getSurvey : {}",memberId);
        List<SurveyResponse> surveyList = surveyService.getMysurvey(memberId);

        List<SurveyResponse> result = new ArrayList<>();
        result.addAll(surveyList);

        return new BaseResponse<>(result);
    }

    @GetMapping("/surveyPage")
    public BaseResponse<List<SurveyResponse>> getAllSurvey() {
        List<SurveyResponse> surveyList = surveyService.getAllSurveys();
        return new BaseResponse<>(surveyList);
    }

    //설문지 삭제 api
    @DeleteMapping("/surveyPage/{surveyId}/{memberId}")
    public BaseResponse<DeleteSurveyResponse> deleteSurvey (@PathVariable(name = "surveyId") Long surveyId, @PathVariable(name = "memberId") String memberId){
        DeleteSurveyResponse response = surveyService.deleteSurvey(surveyId, memberId);

        logger.info("[delete Survey]", response);
        return new BaseResponse<>(response);
    }

//    @GetMapping("/survey/{surveyId}")
//    public BaseResponse<SurveyResponse> getSurveyById(@PathVariable(name="surveyId") Long surveyId){
//        logger.info("[SurveyController] getSurveyById - surveyId: {}",surveyId);
//        SurveyResponse response=surveyService.getSurvey(surveyId);
//
//        return new BaseResponse<>(response);
//    }



    /**
     * 정규
     * 설문지 상태바꾸기 api
     * @param surveyId
     * @return
     */
    @PutMapping("/surveyPage/{surveyId}")
    public BaseResponse<FinishSurveyResponse> finishSurvey(@PathVariable(name = "surveyId") Long surveyId){

        FinishSurveyResponse response = surveyService.finishSurvey(surveyId);

        return new BaseResponse<>(response);
    }

 }
