package surfy.comfy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.result.GetSubmitListResponse;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadSurveyService {

    private final SurveyRepository surveyRepository;

    @SneakyThrows
    @Transactional
    public String getSurveydata(Long surveyId,Boolean loadAnswer,Long submitid){
        ObjectMapper mapper=new ObjectMapper();
        Survey survey= surveyRepository.findSurveysById(surveyId);
        String str_json=mapper.writeValueAsString(new GetSurveyDataResponse(survey,loadAnswer,submitid));
        return str_json;
    }

    @SneakyThrows
    @Transactional
    public String getsubmitlist(Long surveyId){
        ObjectMapper mapper=new ObjectMapper();
        Survey survey= surveyRepository.findSurveysById(surveyId);
        String str_json=mapper.writeValueAsString(new GetSubmitListResponse(survey));
        return str_json;
    }
}