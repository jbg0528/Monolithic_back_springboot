package surfy.comfy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.result.GetSubmitListResponse;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.data.survey.GetSurveyFinishTime;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import surfy.comfy.type.SurveyType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadSurveyService {

    private final SurveyRepository surveyRepository;

    @SneakyThrows
    @Transactional
    public GetSurveyDataResponse getSurveydata(Long surveyId,Boolean loadAnswer,Long submitid){
        Survey survey= surveyRepository.findSurveysById(surveyId);
        GetSurveyDataResponse ret = new GetSurveyDataResponse(survey,loadAnswer,submitid);
        return ret;
    }

    @SneakyThrows
    @Transactional
    public GetSubmitListResponse getsubmitlist(Long surveyId){
        Survey survey= surveyRepository.findSurveysById(surveyId);
        GetSubmitListResponse ret = new GetSubmitListResponse(survey);
        return ret;
    }
}
