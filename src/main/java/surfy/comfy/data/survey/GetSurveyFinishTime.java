package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.Survey;

@Data
public class GetSurveyFinishTime {
    private String status;
    private String start;
    private String end;

    public GetSurveyFinishTime(Survey survey) {
        this.status = String.valueOf(survey.getStatus());
        this.start = String.valueOf(survey.getStart());
        this.end = String.valueOf(survey.getEnd());
    }
}