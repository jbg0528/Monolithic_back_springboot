package surfy.comfy.data.result;

import lombok.Data;
import net.minidev.json.JSONArray;
import surfy.comfy.entity.Survey;

@Data
public class GetSubmitListResponse {
    private JSONArray submitlist;

    public GetSubmitListResponse(Survey survey){
        submitlist=new JSONArray();
        for(int i=0;i<survey.getQuestions().size();i++){
            for(int k=0;k<survey.getQuestions().get(i).getAnswers().size();k++){
                int t=0;
                for(;t<submitlist.size();t++){
                    if(submitlist.get(t)==survey.getQuestions().get(i).getAnswers().get(k).getSubmit()){
                        break;
                    }
                }
                if(t==submitlist.size()){
                    submitlist.add(survey.getQuestions().get(i).getAnswers().get(k).getSubmit());
                }
            }
        }
    }
}