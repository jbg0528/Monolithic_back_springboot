package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.ThumbnailRequest;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import surfy.comfy.type.QuestionType;
import surfy.comfy.type.SurveyType;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CreateSurveyController {

    private final EntityManager em;
    private final MemberRepository memberRepository;
    private final AnswerRespository answerRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final GridRepository gridRepository;
    private final EssayRepository essayRepository;
    @SneakyThrows
    @Transactional
    @PostMapping("/createsurvey/{memberEmail}")
    public Long CreateSurvey(@RequestBody String data, @PathVariable(name="memberEmail")String memberEmail){
        Optional<Member> loadmember= memberRepository.findByEmail(memberEmail);
        Member member=loadmember.get();

        JSONParser parser = new JSONParser();
        JSONObject json=(JSONObject)parser.parse(data);

        String intro0=String.valueOf(json.get("intro0"));
        String intro1=String.valueOf(json.get("intro1"));

        Survey survey=new Survey();

        survey.setTitle(intro0);
        survey.setContents(intro1);
        survey.setMember(member);

        String endtime=String.valueOf(json.get("endtime"));
        if(endtime.equals("not")){
            survey.setStatus(SurveyType.notFinish);
        }
        else{
            survey.setStatus(SurveyType.surveying);
            LocalDate end=LocalDate.parse(endtime);
            survey.setEnd(end.atTime(0,0));
        }
        CreateSurveyDB(json,survey,member);

        return survey.getId();
    }

    /**
     * minseo
     * @param request
     * @return
     */
    @PostMapping("/createsurvey")
    public BaseResponse<String> postThumbnail(@RequestBody ThumbnailRequest request){
        System.out.println("email: "+request.getEmail());
        System.out.println("imgSrc: "+request.getImgSrc());
        System.out.println("imgSrc: "+request.getBgColor());

        String binaryData=request.getImgSrc();

        return new BaseResponse<>("ggg");
    }

    /**
     * minseo
     * @param request
     * @return
     */
    @PatchMapping("/createsurvey")
    public BaseResponse<String> patchThumbnail(@RequestBody ThumbnailRequest request){
        System.out.println("email: "+request.getEmail());
        System.out.println("imgSrc: "+request.getImgSrc());
        System.out.println("bgColor: "+request.getBgColor());

        String binaryData=request.getImgSrc();

        return new BaseResponse<>("ggg");
    }


    @SneakyThrows
    @Transactional
    @PostMapping("/respondentSurvey/{surveyId}")
    public void ResponseAnswer(@RequestBody String data,@PathVariable(name="surveyId")Long surveyId){
        JSONParser parser = new JSONParser();
        JSONObject json=(JSONObject)parser.parse(data);
        Survey survey=surveyRepository.findSurveysById(surveyId);

        CreateAnswerDB(json,survey);
    }



    @SneakyThrows
    @Transactional
    @PostMapping("/editsurvey/{surveyId}/{memberEmail}")
    public Long EditSurvey(@RequestBody String data,@PathVariable(name="surveyId")Long surveyId, @PathVariable(name="memberEmail")String memberEmail){
        Optional<Member> loadmember= memberRepository.findByEmail(memberEmail);
        Member member=loadmember.get();

        if(memberEmail.equals(surveyRepository.findSurveysById(surveyId).getMember().getEmail())){
            Survey survey=surveyRepository.findSurveysById(surveyId);

            JSONParser parser = new JSONParser();
            JSONObject json=(JSONObject)parser.parse(data);

            String endtime=String.valueOf(json.get("endtime"));
            if(endtime.equals("not")){
                survey.setStatus(SurveyType.notFinish);
            }
            else{
                survey.setStatus(SurveyType.surveying);
            }

            EditSurveyDB(json,survey,member);
            return survey.getId();
        }
        else{
            Survey survey=new Survey();

            JSONParser parser = new JSONParser();
            JSONObject json=(JSONObject)parser.parse(data);

            String intro0=String.valueOf(json.get("intro0"));
            String intro1=String.valueOf(json.get("intro1"));

            survey.setTitle(intro0);
            survey.setContents(intro1);
            survey.setMember(member);

            String endtime=String.valueOf(json.get("endtime"));
            if(endtime.equals("not")){
                survey.setStatus(SurveyType.notFinish);
            }
            else{
                survey.setStatus(SurveyType.surveying);
            }

            CreateSurveyDB(json,survey,member);
            return survey.getId();
        }
    }
    @Transactional
    public void CreateSurveyDB(JSONObject json,Survey survey,Member member){
        JSONArray ques_list=(JSONArray) json.get("ques_list");

        for(int i=0;i<ques_list.size();i++){
            Question question=new Question();

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
                }

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
                }

                JSONArray choice_list=(JSONArray) ques_type.get("choice_list");
                for(int k=0;k<choice_list.size();k++){
                    Grid grid=new Grid();

                    JSONObject choice_item=(JSONObject) choice_list.get(k);
                    String text=String.valueOf(choice_item.get("text"));
                    int choice_id=(int) choice_item.get("id");

                    grid.setQuestion(question);
                    grid.setContents(text);
                    grid.setSurvey(survey);
                    em.persist(grid);
                }
            }
            else if(ques_type_id==3){
                Essay essay=new Essay();
                essay.setContents(ques);
                essay.setMember(member);
                em.persist(essay);

                question.setQuestionType(QuestionType.주관식);
            }

            question.setContents(ques);
            question.setSurvey(survey);
            em.persist(question);
        }
        em.persist(survey);
        em.flush();
    }

    public void CreateAnswerDB(JSONObject json,Survey survey){
        JSONArray ques_list=(JSONArray) json.get("ques_list");

        List<Question> Ques_list=questionRepository.findAllBySurvey_Id(survey.getId());

        int t=0;
        for(int i=0;i<Ques_list.size();i++){
            Question question= Ques_list.get(i);

            if(question.getQuestionType()==QuestionType.만족도){
                Answer answer=new Answer();
                answer.setSurvey(survey);
                answer.setQuestion(question);

                Long satis = Long.parseLong((String) json.get("satis"));
                Satisfaction satisfaction=new Satisfaction();
                satisfaction.setQuestion(question);
                satisfaction.setPercent(satis);
                satisfaction.setSurvey(survey);
                em.persist(satisfaction);

                answer.setSatisfaction(satisfaction);
                em.persist(answer);
                continue;
            }
            List<Grid> gridList=gridRepository.findAllByQuestion_Id(question.getId());
            List<Option> optionList=optionRepository.findAllByQuestion_Id(question.getId());
            List<Essay> essayList=essayRepository.findAllByQuestion_Id(question.getId());

            JSONObject ques_item=(JSONObject) ques_list.get(t);
            t++;
            String ques=String.valueOf(ques_item.get("ques"));
            int ques_id=(int) ques_item.get("id");

            JSONObject type=(JSONObject) ques_item.get("type");
            int ques_type_id=(int) type.get("id");

            if(ques_type_id==1){
                JSONObject ques_type=(JSONObject) type.get("que_type");
                JSONArray ans_list=(JSONArray) ques_type.get("ans_list");
                int choice_value=(int)((JSONObject)ans_list.get(0)).get("choice_value");
                for(int k=0;k<ans_list.size();k++){
                    JSONObject ans_item=(JSONObject) ans_list.get(k);
                    int ans_id=(int) ans_item.get("id");

                    if(choice_value==ans_id+1){
                        Answer answer=new Answer();
                        answer.setSurvey(survey);
                        answer.setQuestion(question);

                        answer.setOption(optionList.get(k));
                        em.persist(answer);
                        break;
                    }
                }
            }
            else if(ques_type_id==2){
                JSONObject ques_type=(JSONObject) type.get("que_type");
                JSONArray ans_list=(JSONArray) ques_type.get("ans_list");
                JSONArray choice_list=(JSONArray) ques_type.get("choice_list");
                for(int k=0;k<ans_list.size();k++){
                    int choice_value=(int)((JSONObject)ans_list.get(k)).get("choice_value");
                    JSONObject ans_item=(JSONObject) ans_list.get(k);
                    int ans_id=(int) ans_item.get("id");

                    for(int r=0;r<choice_list.size();r++){
                        JSONObject choice_item=(JSONObject) choice_list.get(r);
                        int choice_id=(int) choice_item.get("id");

                        if(choice_value==choice_id+1){
                            Answer answer=new Answer();
                            answer.setSurvey(survey);
                            answer.setQuestion(question);

                            answer.setOption(optionList.get(r));
                            answer.setGrid(gridList.get(k));
                            em.persist(answer);
                            break;
                        }
                    }
                }
            }
            else if(ques_type_id==3){
                Answer answer=new Answer();
                answer.setSurvey(survey);
                answer.setQuestion(question);

                answer.setEssay(essayList.get(0));
                em.persist(answer);
            }
        }
        em.flush();
    }

    @Transactional
    public void EditSurveyDB(JSONObject json,Survey survey,Member member){
        List<Question> ques_list=questionRepository.findAllBySurvey_Id(survey.getId());

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for(int i=0;i<ques_list.size();i++){

            List<Option> opt_list=optionRepository.findAllByQuestion_Id(ques_list.get(i).getId());
            List<Grid> grid_list=gridRepository.findAllByQuestion_Id(ques_list.get(i).getId());
            List<Essay> essay_list=essayRepository.findAllByQuestion_Id(ques_list.get(i).getId());

            optionRepository.deleteAllInBatch(opt_list);
            gridRepository.deleteAllInBatch(grid_list);
            essayRepository.deleteAllInBatch(essay_list);
        }
        questionRepository.deleteAllInBatch(ques_list);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        CreateSurveyDB(json, survey, member);
    }

}