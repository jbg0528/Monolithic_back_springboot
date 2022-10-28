package surfy.comfy.data.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import surfy.comfy.entity.Question;
import surfy.comfy.type.QuestionType;
@Data
public class GetQuestionTypeResponse {
    private Long id;
    private String name;
    private Long choice_type;
    private JSONArray choice_value;
    private String answer;
    private JSONArray fileList;

    @SneakyThrows
    public GetQuestionTypeResponse(Question question,Boolean loadAnswer,Long submitid){
        ObjectMapper mapper=new ObjectMapper();
        JSONParser parser=new JSONParser();
        if(question.getQuestionType()== QuestionType.객관식_단일 ||
                question.getQuestionType()== QuestionType.객관식_중복){
            this.id=1L;
            this.name="객관식";
            if(question.getQuestionType()==QuestionType.객관식_단일){
                this.choice_type=0L;
            }
            else{
                this.choice_type=1L;
            }
            this.choice_value=new JSONArray();
            if(loadAnswer){
                for(int i=0;i<question.getAnswers().size();i++){
                    if(question.getAnswers().get(i).getSubmit()==submitid){
                        String str_answer=mapper.writeValueAsString(new GetChoiceAnswerResponse(question.getAnswers().get(i)));
                        JSONObject json_answer=(JSONObject) parser.parse(str_answer);
                        this.choice_value.add(json_answer);
                    }
                }
            }

        }
        else if(question.getQuestionType()== QuestionType.객관식_그리드_단일 ||
                question.getQuestionType()== QuestionType.객관식_그리드_중복){
            this.id=2L;
            this.name="객관식 Grid";
            if(question.getQuestionType()==QuestionType.객관식_그리드_단일){
                this.choice_type=0L;
            }
            else{
                this.choice_type=1L;
            }
            this.choice_value=new JSONArray();
            if(loadAnswer){
                for(int i=0;i<question.getAnswers().size();i++){
                    if(question.getAnswers().get(i).getSubmit()==submitid) {
                        String str_answer = mapper.writeValueAsString(new GetChoiceAnswerResponse(question.getAnswers().get(i)));
                        JSONObject json_answer = (JSONObject) parser.parse(str_answer);
                        this.choice_value.add(json_answer);
                    }
                }
            }
        }
        else if(question.getQuestionType()==QuestionType.주관식){
            this.id=3L;
            this.name="주관식";
            try{
                if(loadAnswer){
                    for(int i=0;i<question.getAnswers().size();i++){
                        if(question.getAnswers().get(i).getSubmit()==submitid) {
                            this.answer=question.getAnswers().get(i).getEssay().getContents();
                            break;
                        }
                    }

                }
            }
            catch(Exception e){
                this.answer="";
            }
        }
        else if(question.getQuestionType()==QuestionType.선형배율){
            this.id=4L;
            this.name="선형 배율";
            //this.answer
        }
        else if(question.getQuestionType()==QuestionType.파일){
            this.id=5L;
            this.name="파일 업로드";
            //this.fileList
        }

    }
}