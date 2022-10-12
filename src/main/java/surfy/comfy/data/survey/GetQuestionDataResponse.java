package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Grid;
import surfy.comfy.entity.Linear;
import surfy.comfy.entity.Option;
import surfy.comfy.entity.Question;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class GetQuestionDataResponse {

    private Question question;
    private List<Option> optionList;
    private List<Grid> gridList;
    private Linear linear;
}
