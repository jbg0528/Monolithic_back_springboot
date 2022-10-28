package surfy.comfy.data.manage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Survey;
import surfy.comfy.type.SurveyType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class SurveyResponse {

    private String title;
    private String content;
    private LocalDateTime start;
    private Long surveyId;
    private SurveyType status; // 설문 상태
    private String thumbnail;
    private int satisfaction;

    public SurveyResponse(Survey survey){
        this.title = survey.getTitle();
        this.surveyId = survey.getId();
        this.content = survey.getContents();
        this.status = survey.getStatus();
        this.start = survey.getStart();
        this.thumbnail= "images/"+survey.getThumbnail()+".jpg";
    }

    public SurveyResponse(Survey survey,int average){
        this.title = survey.getTitle();
        this.surveyId = survey.getId();
        this.content = survey.getContents();
        this.status = survey.getStatus();
        this.start = survey.getStart();
        this.thumbnail= "images/"+survey.getThumbnail()+".jpg";
        this.satisfaction=average;
    }

}
