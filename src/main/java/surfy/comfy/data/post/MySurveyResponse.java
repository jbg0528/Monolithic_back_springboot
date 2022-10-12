package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Survey;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MySurveyResponse {
    private Long surveyId;
    private Long memberId;
    private String title;
    public MySurveyResponse(Survey survey){
        this.surveyId=survey.getId();
        this.memberId=survey.getMember().getId();
        this.title=survey.getTitle();

    }
}
