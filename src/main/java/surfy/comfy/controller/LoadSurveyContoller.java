package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.entity.Member;
import surfy.comfy.entity.Survey;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.SurveyRepository;
import surfy.comfy.service.LoadSurveyService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LoadSurveyContoller {

    private final LoadSurveyService loadSurveyService;
    private final Logger logger= LoggerFactory.getLogger(LoadSurveyContoller.class);
    @SneakyThrows
    @GetMapping("/createSurvey/{surveyId}")
    public BaseResponse<JSONObject> SendEditSurveyData(@PathVariable(name="surveyId")Long surveyId){
        String result= loadSurveyService.getSurveydata(surveyId,false,null);
        logger.info("editSurvey - surveyId: {}",surveyId);
        JSONParser jsonparser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonparser.parse(result);

        System.out.println(result);

        return new BaseResponse<>(jsonObject);
    }

    @SneakyThrows
    @GetMapping("/respondentSurvey/{surveyId}")
    public BaseResponse<JSONObject> SendRespondentSurveyData(@PathVariable(name="surveyId")Long surveyId){
        String result= loadSurveyService.getSurveydata(surveyId,false,null);

        JSONParser jsonparser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonparser.parse(result);

        System.out.println(result);

        return new BaseResponse<>(jsonObject);
    }

    @SneakyThrows
    @GetMapping("/answerSurvey/{surveyId}/{submitId}")
    public BaseResponse<JSONObject> SendSurveyAnswerData(@PathVariable(name="surveyId")Long surveyId,@PathVariable(name="submitId")Long submitId){
        String result= loadSurveyService.getSurveydata(surveyId,true,submitId);

        JSONParser jsonparser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonparser.parse(result);

        System.out.println(result);

        return new BaseResponse<>(jsonObject);
    }

    @SneakyThrows
    @GetMapping("/answerSurvey/{surveyId}")
    public BaseResponse<JSONObject> SendSurveyAnswerData(@PathVariable(name="surveyId")Long surveyId){
        String result= loadSurveyService.getsubmitlist(surveyId);

        JSONParser jsonparser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonparser.parse(result);

        System.out.println(result);

        return new BaseResponse<>(jsonObject);
    }

}