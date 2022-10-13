package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.entity.Member;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.service.LoadSurveyService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LoadSurveyContoller {

    private final LoadSurveyService loadSurveyService;
    @SneakyThrows
    @GetMapping("/editsurvey/{surveyId}")
    public BaseResponse<JSONObject> SendEditSurveyData(@PathVariable(name="surveyId")Long surveyId){
        String result= loadSurveyService.getSurveydata(surveyId);

        JSONParser jsonparser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonparser.parse(result);

        System.out.println(result);

        return new BaseResponse<>(jsonObject);
    }

    @SneakyThrows
    @GetMapping("/respondentSurvey/{surveyId}")
    public BaseResponse<JSONObject> SendRespondentSurveyData(@PathVariable(name="surveyId")Long surveyId){
        String result= loadSurveyService.getSurveydata(surveyId);

        JSONParser jsonparser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonparser.parse(result);

        System.out.println(result);

        return new BaseResponse<>(jsonObject);
    }

}