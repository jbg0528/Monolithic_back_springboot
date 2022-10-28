package surfy.comfy.data.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import surfy.comfy.entity.Survey;
import surfy.comfy.type.QuestionType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetSurveyDataResponse {
    private String intro0;
    private String intro1;
    private JSONArray ques_list;
    private JSONArray ans_list;
    private JSONArray choice_list;
    private Long satis;
    @SneakyThrows
    public GetSurveyDataResponse(Survey survey,Boolean loadAnswer,Long submitid){
        ObjectMapper mapper=new ObjectMapper();
        JSONParser parser=new JSONParser();
        this.intro0=survey.getTitle();
        this.intro1=survey.getContents();
        this.ques_list=new JSONArray();
        this.ans_list=new JSONArray();
        this.choice_list=new JSONArray();

        List<JSONObject> ans_temp=new ArrayList<>();
        List<JSONObject> cho_temp=new ArrayList<>();
        for(int i=0;i<survey.getQuestions().size();i++){
            if(survey.getQuestions().get(i).getQuestionType()== QuestionType.만족도){
                try{
                    if(loadAnswer){

                        for(int k=0;k<survey.getQuestions().get(i).getAnswers().size();k++){
                            System.out.println(survey.getQuestions().get(i).getAnswers().get(k).getSubmit());
                            if(survey.getQuestions().get(i).getAnswers().get(k).getSubmit()==submitid) {
                                this.satis=survey.getQuestions().get(i).getAnswers().get(k).getSatisfaction().getPercent();
                                break;
                            }
                        }
                    }

                }
                catch(Exception e){
                    this.satis=null;
                }
                continue;
            }
            String str_ques_list=mapper.writeValueAsString(new GetQuestionResponse(survey.getQuestions().get(i),loadAnswer,submitid));
            JSONObject json_ques_list=(JSONObject) parser.parse(str_ques_list);
            this.ques_list.add(json_ques_list);

            for(int k=0;k<survey.getQuestions().get(i).getOptions().size();k++){
                String str_opt_list=mapper.writeValueAsString(new GetOptionResponse(survey.getQuestions().get(i).getOptions().get(k)));
                JSONObject json_opt_list=(JSONObject) parser.parse(str_opt_list);

                int t=0;
                for(;t<ans_temp.size();t++){
                    if((int)ans_temp.get(t).get("id")>(int)json_opt_list.get("id")){
                        ans_temp.add(t,json_opt_list);
                        this.ans_list.add(t,json_opt_list);
                        break;
                    }
                }
                if(t==ans_temp.size()){
                    ans_temp.add(json_opt_list);
                    this.ans_list.add(json_opt_list);
                }
            }
            for(int k=0;k<survey.getQuestions().get(i).getGrids().size();k++){
                String str_grid_list=mapper.writeValueAsString(new GetGridResponse(survey.getQuestions().get(i).getGrids().get(k)));
                JSONObject json_grid_list=(JSONObject) parser.parse(str_grid_list);

                int t=0;
                for(;t<cho_temp.size();t++){
                    if((int)cho_temp.get(t).get("id")>(int)json_grid_list.get("id")){
                        cho_temp.add(t,json_grid_list);
                        this.choice_list.add(t,json_grid_list);
                        break;
                    }
                }
                if(t==cho_temp.size()){
                    cho_temp.add(json_grid_list);
                    this.choice_list.add(json_grid_list);
                }
            }
        }

    }
}