package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.manage.DeleteSurveyResponse;
import surfy.comfy.data.manage.FinishSurveyResponse;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.data.post.MySurveyResponse;
import surfy.comfy.data.survey.GetSurveyResponse;
import surfy.comfy.data.survey.PostSurveyResponse;
import surfy.comfy.entity.Grid;
import surfy.comfy.entity.Option;
import surfy.comfy.entity.Question;
import surfy.comfy.entity.Survey;
import surfy.comfy.repository.*;
import surfy.comfy.type.SurveyType;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final GridRepository gridRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final Logger logger= LoggerFactory.getLogger(SurveyService.class);

    /**
     * minseo
     * 마음에 드는 설문지 임시 저장
     * @param surveyId
     * @param memberId
     * @return
     */
    @Transactional
    public PostSurveyResponse makeSurvey(Long surveyId, Long memberId){
        List<Question> questionList=questionRepository.findAllBySurvey_Id(surveyId);
        List<Grid> gridList=gridRepository.findAllBySurvey_Id(surveyId);
        List<Option> optionList=optionRepository.findAllBySurvey_Id(surveyId);
        Survey createdSurvey=surveyRepository.findById(surveyId).get(); // 커뮤니티 게시글에서 마음에 들어서 만드려고 하는 설문지

        // 내 설문지로 생성
        Survey survey=new Survey();
        survey.setContents(createdSurvey.getContents());
        survey.setTitle(createdSurvey.getTitle());
        survey.setMember(memberRepository.findById(memberId).get());
        survey.setStatus(SurveyType.notFinish);
        Long newSurveyId=surveyRepository.save(survey).getId();

        for(Question q:questionList){
            Question question=new Question();
            question.setContents(q.getContents());
            question.setSurvey(surveyRepository.findById(newSurveyId).get());
            question.setQuestionType(q.getQuestionType());
            Long newQuestionId=questionRepository.save(question).getId();

            for(Grid g:gridList){
                Grid grid=new Grid();
                grid.setContents(g.getContents());
                grid.setQuestion(questionRepository.findById(newQuestionId).get());
                grid.setSurvey(surveyRepository.findById(newSurveyId).get());

                gridRepository.save(grid);
            }

            for(Option o:optionList){
                Option option=new Option();
                option.setContents(o.getContents());
                option.setSurvey(surveyRepository.findById(newSurveyId).get());
                option.setQuestion(questionRepository.findById(newQuestionId).get());

                optionRepository.save(option);
            }
        }



        return new PostSurveyResponse(newSurveyId,memberId);

    }

    //멤버id - 설문지 가져오기
    @Transactional
    public List<SurveyResponse> getMysurvey(Long memberId){

        List<Survey> mySurveyList = surveyRepository.findAllByMember_Id(memberId);
        List<SurveyResponse> mySurvey = mySurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return mySurvey;
    }

    //설문지 전체 가져오기
    @Transactional
    public List<SurveyResponse> getAllSurveys() {
        List<Survey> SurveyList = surveyRepository.findAll();
        List<SurveyResponse> Surveys = SurveyList.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return Surveys;
    }

    //설문지 삭제
    @Transactional
    public DeleteSurveyResponse deleteSurvey(Long surveyId, String memberId){
        Survey survey = surveyRepository.findById(surveyId).get();

        surveyRepository.delete(survey);
        return new DeleteSurveyResponse(surveyId, Long.parseLong(memberId));
    }


    /**
     * minseo
     * 주기적으로 설문지 상태 업데이트
     */
//    @Scheduled(cron = "0 00 5 * * *") // 매일 오전 5:00에 실행되는 스케줄링
    @Scheduled(cron = "0 0 0/2 * * *") // 2시간마다 실행
    public void updateSurveyStatus(){
//        long time = System.currentTimeMillis();
//        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd kk:mm:ss E요일");
//        String str = dayTime.format(new Date(time));
        LocalDate now= LocalDate.now();

        logger.info("현재 시각: {}",now);

        List<Survey> surveyList=surveyRepository.findAll();
        for(Survey survey:surveyList){
            if(LocalDate.from(survey.getEnd()).isBefore(now)) survey.setStatus(SurveyType.finish);
        }
    }

//    /**
//     * surveyId로 설문지 정보 조회
//     */
//    @Transactional
//    public SurveyResponse getSurvey(Long surveyId){
//        Survey survey=surveyRepository.findById(surveyId).get();
//
//        return new SurveyResponse(survey);
//
//    }

    //설문지 상태 update
    @Transactional
    public FinishSurveyResponse finishSurvey(Long surveyId){
        Survey survey = surveyRepository.findById(surveyId).get();

        survey.setStatus(SurveyType.finish);

        return new FinishSurveyResponse(surveyId);
    }

    //Survey id - 설문지 가져오기
    @Transactional
    public List<SurveyResponse> getSurvey(Long surveyId){

        List<Survey> Survey = surveyRepository.findSurveyById(surveyId);
        List<SurveyResponse> mySurvey = Survey.stream()
                .map(p -> new SurveyResponse(p))
                .collect(Collectors.toList());

        return mySurvey;
    }
}
