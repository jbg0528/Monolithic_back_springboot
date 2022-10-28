package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.ThumbnailRequest;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.data.survey.GetSurveyDataResponse;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import surfy.comfy.service.SurveyService;
import surfy.comfy.type.QuestionType;
import surfy.comfy.type.SurveyType;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CreateSurveyController {

    private final EntityManager em;
    private final MemberRepository memberRepository;
    private final AnswerRespository answerRepository;

    private final SatisfactionRepository satisfactionRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final GridRepository gridRepository;
    private final EssayRepository essayRepository;
    private final SurveyService surveyService;
    @SneakyThrows
    @Transactional
    @PostMapping("/createsurvey/{memberEmail}")
    public BaseResponse<Long> CreateSurvey(@RequestBody String data, @PathVariable(name="memberEmail")String memberEmail){

        System.out.println("memberEmail: "+memberEmail);
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
        System.out.println("surveyId"+survey.getId());

        return new BaseResponse<>(survey.getId());
    }

    /**
     * minseo
     * @param request
     * @return
     */
//    @PatchMapping("/thumbnail")
//    public BaseResponse<String> postThumbnail(@RequestBody ThumbnailRequest request){
//        System.out.println("email: "+request.getEmail());
//        System.out.println("imgSrc: "+request.getImgSrc());
//        System.out.println("bgColor: "+request.getBgColor());
//        System.out.println("surveyId: "+request.getSurveyId());
//
//        surveyService.patchSurveyThumbnail(request);
//
//        return new BaseResponse<>("ggg");
//    }

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
    @PostMapping("/createSurvey/{surveyId}/{memberEmail}")
    public BaseResponse<Long> EditSurvey(@RequestBody String data,@PathVariable(name="surveyId")Long surveyId, @PathVariable(name="memberEmail")String memberEmail){
        Optional<Member> loadmember= memberRepository.findByEmail(memberEmail);
        Member member=loadmember.get();
        Long returnId;
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
            returnId=survey.getId();
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
            returnId=survey.getId();
        }
        return new BaseResponse<>(returnId);
    }
    @Transactional
    public void CreateSurveyDB(JSONObject json,Survey survey,Member member){
        JSONArray ques_list=(JSONArray) json.get("ques_list");
        JSONArray ans_list=(JSONArray) json.get("ans_list");
        JSONArray choice_list=(JSONArray) json.get("choice_list");
        for(int i=0;i<ques_list.size();i++){
            Question question=new Question();

            //Answer answer=new Answer();

            JSONObject ques_item=(JSONObject) ques_list.get(i);
            String ques=String.valueOf(ques_item.get("ques"));
            int ques_id=(int) ques_item.get("id");

            JSONObject type=(JSONObject) ques_item.get("type");
            int ques_type_id=(int) type.get("id");

            if(ques_type_id==1){ //객관식
                int choice_type=(int) type.get("choice_type");

                if(choice_type==0){
                    question.setQuestionType(QuestionType.객관식_단일);
                }
                else{
                    question.setQuestionType(QuestionType.객관식_중복);
                }

                for(int k=0;k<ans_list.size();k++){ //해당 Question의 ans_list 불러오기
                    JSONObject ans_item=(JSONObject) ans_list.get(k);
                    int ans_id=(int) ans_item.get("id");
                    int root_id=(int) ans_item.get("rootid");

                    if(root_id==ques_id){
                        Option option=new Option();
                        String text=String.valueOf(ans_item.get("value"));

                        option.setQuestion(question);
                        option.setContents(text);
                        option.setSurvey(survey);
                        em.persist(option);
                    }
                }
            }
            else if(ques_type_id==2){ //객관식 Grid
                int choice_type=(int) type.get("choice_type");

                if(choice_type==0){
                    question.setQuestionType(QuestionType.객관식_그리드_단일);
                }
                else{
                    question.setQuestionType(QuestionType.객관식_그리드_중복);
                }
                for(int k=0;k<ans_list.size();k++){ //해당 Question의 ans_list 불러오기
                    JSONObject ans_item=(JSONObject) ans_list.get(k);
                    int ans_id=(int) ans_item.get("id");
                    int root_id=(int) ans_item.get("rootid");

                    if(root_id==ques_id){
                        Option option=new Option();
                        String text=String.valueOf(ans_item.get("value"));

                        option.setQuestion(question);
                        option.setContents(text);
                        option.setSurvey(survey);
                        em.persist(option);
                    }
                }

                for(int k=0;k<choice_list.size();k++){ //해당 Question의 choice_list 불러오기
                    JSONObject choice_item=(JSONObject) choice_list.get(k);
                    int cho_id=(int) choice_item.get("id");
                    int root_id=(int) choice_item.get("rootid");

                    if(root_id==ques_id){
                        Grid grid=new Grid();
                        String text=String.valueOf(choice_item.get("value"));

                        grid.setQuestion(question);
                        grid.setContents(text);
                        grid.setSurvey(survey);
                        em.persist(grid);
                    }
                }
            }
            else if(ques_type_id==3){ //주관식 option이 하나기 때문에 ans_list를 탐색할 이유가 없다
                Essay essay=new Essay();
                essay.setQuestion(question);
                essay.setMember(member);
                em.persist(essay);

                question.setQuestionType(QuestionType.주관식);
            }

            question.setContents(ques);
            question.setSurvey(survey);
            em.persist(question);
        }
        Question question=new Question();
        question.setSurvey(survey);
        question.setQuestionType(QuestionType.만족도);
        em.persist(question);
        em.persist(survey);
        em.flush();
    }

    public void CreateAnswerDB(JSONObject json,Survey survey){
        JSONArray ques_list=(JSONArray) json.get("ques_list");
        List<Question> Ques_list=questionRepository.findAllBySurvey_Id(survey.getId());
        List<Answer> survey_ans_list=answerRepository.getAnswerBySurveyId(survey.getId());
        Long submitid=survey_ans_list.get(survey_ans_list.size()-1).getSubmit()+1;
        int t=0;
        for(int i=0;i<Ques_list.size();i++){
            Question question= Ques_list.get(i);

            if(question.getQuestionType()==QuestionType.만족도){
                Answer answer=new Answer();
                answer.setSurvey(survey);
                answer.setQuestion(question);
                answer.setSubmit(submitid);

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
            int ques_id=question.getId().intValue();

            JSONObject type=(JSONObject) ques_item.get("type");
            int ques_type_id=(int) type.get("id");

            if(ques_type_id==1 || ques_type_id==2){ //객관식 답변
                JSONArray ans_list=(JSONArray) type.get("choice_value");
                for(int k=0;k<ans_list.size();k++){
                    JSONObject ans_item=(JSONObject) ans_list.get(k);
                    int select_id=(int) ans_item.get("selectid");

                    Answer answer=new Answer();
                    answer.setSurvey(survey);
                    answer.setQuestion(question);
                    answer.setSubmit(submitid);

                    if(ques_type_id==2){ //객관식 Grid 답변
                        int ans_id=(int) ans_item.get("rootid");
                        Option select_opt=optionList.stream().filter(s->s.getId()==ans_id).findFirst().get();
                        Grid select_grid=gridList.stream().filter(s->s.getId()==select_id).findFirst().get();
                        answer.setGrid(select_grid);
                        answer.setOption(select_opt);
                    }
                    else{
                        Option select_opt=optionList.stream().filter(s->s.getId()==select_id).findFirst().get();
                        answer.setOption(select_opt);
                    }

                    em.persist(answer);
                }
            }
            else if(ques_type_id==3){ //주관식
                String writing=(String) type.get("answer");

                Answer answer=new Answer();
                answer.setSurvey(survey);
                answer.setQuestion(question);
                answer.setSubmit(submitid);

                Essay essay=essayList.get(0);
                essay.setContents(writing);
                essayList.set(0,essay);
                answer.setEssay(essay);
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