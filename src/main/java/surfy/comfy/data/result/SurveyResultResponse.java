package surfy.comfy.data.result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Survey;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResultResponse {

    private String title;
    private String contents;
    // 문서 타입이 넘어오면 저장해주면 될 듯
    // private String type;

    public SurveyResultResponse(Survey survey){
        this.title = survey.getTitle();
        this.contents = survey.getContents();
        // this.type = survey.getStatus();
    }
}
