package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.service.LoadSurveyService;

@RestController
@RequiredArgsConstructor
public class LoadSurveyContoller {

    private final LoadSurveyService loadSurveyService;

    @SneakyThrows
    @PostMapping("/respondentSurvey")
    public void LoadSurveyData(@RequestBody String data){
        JSONParser parser = new JSONParser();
        JSONObject json=(JSONObject)parser.parse(data);

        String res_type=String.valueOf(json.get("res_type"));
        if(res_type.equals("RespondentSurvey")){

        }
        else if(res_type.equals("CompleteSurvey")){

        }
    }

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
