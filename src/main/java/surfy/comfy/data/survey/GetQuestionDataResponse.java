package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.*;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class GetQuestionDataResponse {

    private Question question;
    private List<Option> optionList;
    private List<Grid> gridList;
    private List<Slider> sliderList;
}
