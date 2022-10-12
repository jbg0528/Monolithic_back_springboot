package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.Survey;

@Data
public class GetSurveyResponse {
    private Long surveyId;
    private String surveyTitle;

    public GetSurveyResponse(Survey survey){
        this.surveyId=survey.getId();
        this.surveyTitle=survey.getTitle();
    }
}
