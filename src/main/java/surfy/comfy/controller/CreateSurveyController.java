package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.post.MyPageResponse;
import surfy.comfy.data.post.PostResponse;
import surfy.comfy.entity.*;
import surfy.comfy.service.BookmarkService;
import surfy.comfy.service.PostService;
import surfy.comfy.type.QuestionType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CreateSurveyController {

    private final EntityManager em;

    @SneakyThrows
    @Transactional
    @PostMapping("/createsurvey")
    public void CreateSurvey(@RequestBody String data, Member member){
        JSONParser parser = new JSONParser();
        JSONObject json=(JSONObject)parser.parse(data);

        String intro0=String.valueOf(json.get("intro0"));
        String intro1=String.valueOf(json.get("intro1"));

        JSONArray ques_list=(JSONArray) json.get("ques_list");

        Survey survey=new Survey();
        List<Question> questionList=new ArrayList<>();

        survey.setTitle(intro0);
        survey.setContents(intro1);

        for(int i=0;i<ques_list.size();i++){
            Question question=new Question();

            List<Grid> gridList=new ArrayList<>();
            List<Option> optionList=new ArrayList<>();
            List<Essay> essayList=new ArrayList<>();

            //Answer answer=new Answer();

            JSONObject ques_item=(JSONObject) ques_list.get(i);
            String ques=String.valueOf(ques_item.get("ques"));
            int ques_id=(int) ques_item.get("id");

            JSONObject type=(JSONObject) ques_item.get("type");
            int ques_type_id=(int) type.get("id");

            if(ques_type_id==1){
                JSONObject ques_type=(JSONObject) type.get("que_type");
                int choice_type=(int) ques_type.get("choice_type");

                if(choice_type==0){
                    question.setQuestionType(QuestionType.객관식_단일);
                }
                else{
                    question.setQuestionType(QuestionType.객관식_중복);
                }
                JSONArray ans_list=(JSONArray) ques_type.get("ans_list");

                for(int k=0;k<ans_list.size();k++){
                    Option option=new Option();

                    JSONObject ans_item=(JSONObject) ans_list.get(k);
                    String text=String.valueOf(ans_item.get("text"));
                    int ans_id=(int) ans_item.get("id");

                    option.setQuestion(question);
                    option.setContents(text);
                    option.setSurvey(survey);

                    em.persist(option);
                    optionList.add(option);
                }

                question.setOptions(optionList);

            }
            else if(ques_type_id==2){
                JSONObject ques_type=(JSONObject) type.get("que_type");
                int choice_type=(int) ques_type.get("choice_type");

                if(choice_type==0){
                    question.setQuestionType(QuestionType.객관식_그리드_단일);
                }
                else{
                    question.setQuestionType(QuestionType.객관식_그리드_중복);
                }
                JSONArray ans_list=(JSONArray) ques_type.get("ans_list");

                for(int k=0;k<ans_list.size();k++){
                    Option option=new Option();

                    JSONObject ans_item=(JSONObject) ans_list.get(k);
                    String text=String.valueOf(ans_item.get("text"));
                    int ans_id=(int) ans_item.get("id");

                    option.setQuestion(question);
                    option.setContents(text);
                    option.setSurvey(survey);

                    em.persist(option);
                    optionList.add(option);
                }

                JSONObject choice_list=(JSONObject) ques_type.get("choice_list");
                for(int k=0;k<choice_list.size();k++){
                    Grid grid=new Grid();

                    JSONObject choice_item=(JSONObject) choice_list.get(k);
                    String text=String.valueOf(choice_item.get("text"));
                    int choice_id=(int) choice_item.get("id");

                    grid.setQuestion(question);
                    grid.setContents(text);
                    grid.setSurvey(survey);

                    em.persist(grid);
                    gridList.add(grid);
                }

                question.setOptions(optionList);
                question.setGrids(gridList);
            }
            else if(ques_type_id==3){
                Essay essay=new Essay();
                essay.setContents(ques);
                //essay.setMember(member);

                em.persist(essay);
                essayList.add(essay);

                question.setEssays(essayList);
                question.setQuestionType(QuestionType.주관식);
            }

            question.setContents(ques);
            question.setSurvey(survey);

            em.persist(question);
            questionList.add(question);
        }

        survey.setQuestions(questionList);
        //survey.setMember(member);
        em.persist(survey);
        em.flush();

        System.out.println(em);

    }

}