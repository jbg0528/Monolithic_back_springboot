package surfy.comfy.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.post.LoadSurveyResponse;
import surfy.comfy.data.post.MyPageResponse;
import surfy.comfy.data.post.PostResponse;
import surfy.comfy.entity.Member;
import surfy.comfy.service.RespondentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RespondentSurveyContoller {

    private final RespondentService respondentService;

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
    @GetMapping("/respondentSurvey")
    public BaseResponse<JSONObject> SendSurveyData(){
        String result=respondentService.getRespondentSurvey(5L);
        System.out.println(result);
        JSONParser jsonparser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonparser.parse(result);
        System.out.println(jsonObject);

        return new BaseResponse<>(jsonObject);
    }

}
