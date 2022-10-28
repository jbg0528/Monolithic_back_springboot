package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.result.*;
import surfy.comfy.entity.*;
import surfy.comfy.exception.ResourceNotFoundException;
import surfy.comfy.repository.*;
import surfy.comfy.type.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {
    private final ResultRepository resultRepository;
    private final SurveyRepository surveyRepository;

    private final QuestionRepository questionRepository;

    private final IndividualRepository individualRepository;

    private final AnswerRespository answerRespository;

    private final GridRepository gridRepository;

    private final OptionRepository optionRepository;

    public Survey getSurveyById(Long surveyId){
        Survey survey = resultRepository.findById(surveyId)
                .orElseThrow(()-> new ResourceNotFoundException("Survey not exist with id :" + surveyId));
        System.out.println("survey satisfaction: "+survey.getSatisfaction());
        return survey;
    }

    //질문 내용과 해당하는 답변 가져오기
    public List<QuestionAnswerResponse> getQuestionAnswerList(Long surveyId){
        List<Question> questionList = questionRepository.findAllBySurvey_id(surveyId);
        List<QuestionResponse> questionResponseList = questionList.stream()
                .map(p -> new QuestionResponse(p))
                .collect(Collectors.toList());
        List<Answer> answerList = answerRespository.getAnswerBySurveyId(surveyId);

        List<QuestionAnswerResponse> questionAnswerResponseList = new ArrayList<>();

        int grid_count = 0;
        for(int i=0; i<questionList.size(); i++) {
            if (Objects.equals(questionList.get(i).getQuestionType(), QuestionType.객관식_그리드_단일)) {
                List<Grid> gridList = gridRepository.findAllByQuestion_Id(questionList.get(i).getId());
                grid_count = grid_count + gridList.size() -1;
            }
        }
        int user_count = answerList.size() / (questionList.size() + grid_count) ;

        int grid_question = 0;
        for(int i=0; i<questionList.size(); i++){
            QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse();
            questionAnswerResponse.setQuestion(questionResponseList.get(i));
            List<Answer> answers = new ArrayList<>();

            for(int j=0; j<user_count; j++){
                if(Objects.equals(questionList.get(i).getQuestionType(), QuestionType.객관식_그리드_단일)){
                    if(j==0){grid_question++;}
                    for(int k=0; k<grid_count+1; k++){
                        answers.add(answerList.get(i + (j * (questionList.size()+grid_count)) + k));
                    }
                }else{
                    answers.add(answerList.get(i + ((grid_count) * grid_question) + (j * (questionList.size()+grid_count))));
                }
            }
            questionAnswerResponse.setAnswer(answers);
            questionAnswerResponseList.add(questionAnswerResponse);
        }


        return questionAnswerResponseList;
    }

    // 문항별 보기에서 객관식 질문이 있으면 옵션 가져오기
    public List<Option> getOptions(Long surveyId, Long questionId){
        List<Option> optionList = optionRepository.findAllBySurvey_Question_Id(surveyId, questionId);
//        List<Option> rorOptionList = new ArrayList<>();
//        for(int i=0; i<optionList.size(); i++){
//            if(Objects.equals(optionList.get(i).getQuestion().getQuestionType(), QuestionType.객관식_단일)){
//                rorOptionList.add(optionList.get(i));
//            }
//        }
        return optionList;
    }

    // 응답자 수 가져오기 -> 만족도 테이블로 구현 완료
    public List<RespondentsResponse> getRespondents(Long surveyId){
        List<Satisfaction> satisfactionList = individualRepository.findAll(surveyId);
        List<RespondentsResponse> responseList = satisfactionList.stream()
                .map(p -> new RespondentsResponse(p))
                .collect(Collectors.toList());

        return responseList;
    }

    public List<Answer> getAnswers(Long surveyId, Long individualId){
        List<Question> questionList = questionRepository.findAllBySurvey_Id(surveyId);
        List<Answer> answerList = answerRespository.getAnswerBySurveyId(surveyId);

        Long questionSize = (long) questionList.size();
        int count=0;

        for(int i=0; i<questionSize; i++) {
            if (Objects.equals(questionList.get(i).getQuestionType(), QuestionType.객관식_그리드_단일)) {
                List<Grid> gridList = gridRepository.findAllByQuestion_Id(questionList.get(i).getId());
                count = count + gridList.size() -1;
            }
        }
        int result = (int) (individualId * (questionSize + count));
        int start = (int) ((individualId-1)*(questionSize + count)) ;

        List<Answer> selectedAnswers = new ArrayList<>();
        for(int i = start; i< result; i++){
            selectedAnswers.add(answerList.get(i));
        }

//        for(int i=0; i<questionSize; i++){
//            if(Objects.equals(questionList.get(i).getQuestionType(), QuestionType.객관식_그리드_단일)){
//                List<Grid> gridList = answerRespository.getAnswerGrid(questionList.get(i).getId());
//                List<Option> optionList = answerRespository.getAnswerOption(questionList.get(i).getId());
//
//                count = count + gridList.size();
//
//                List<OptionAnswerResponse> optionAnswerResponseList = optionList.stream()
//                        .map(p -> new OptionAnswerResponse(p))
//                        .collect(Collectors.toList());
//
//                List<GridAnswerResponse> gridAnswerResponseList = gridList.stream()
//                        .map(p -> new GridAnswerResponse(p))
//                        .collect(Collectors.toList());
//
//                List<GridOptionAnswerResponse> gridOptionAnswerResponseList = new ArrayList<>();
//
//                for(int j=0; j<gridList.size(); j++){
//                    GridOptionAnswerResponse grid = new GridOptionAnswerResponse();
//                    grid.setGridAnswerResponseList(gridAnswerResponseList.get(j));
//                    grid.setOptionAnswerResponse(optionAnswerResponseList.get(j));
//
//                    gridOptionAnswerResponseList.add(grid);
//                }
//                allAnswerResponseList.setGridOptionAnswerResponse(gridOptionAnswerResponseList);
//
//            }else if(Objects.equals(questionList.get(i).getQuestionType(), QuestionType.객관식_단일)){
//                List<Option> optionList = answerRespository.getOptionAnswer(questionList.get(i).getId());
//
//                List<OptionAnswerResponse> optionAnswerResponseList = optionList.stream()
//                        .map(p -> new OptionAnswerResponse(p))
//                        .collect(Collectors.toList());
//
//                allAnswerResponseList.setOption
//
//            }else if(Objects.equals(questionList.get(i).getQuestionType(), QuestionType.주관식)){
//                List<Essay> essayList = answerRespository.getEssayAnswer(questionList.get(i).getId());
//
//                List<EssayResponse> essayResponseList = essayList.stream()
//                        .map(p->new EssayResponse(p))
//                        .collect(Collectors.toList());
//
//            }else{
//                List<Satisfaction> satisfactionList = answerRespository.getSatisfactionAnswer(questionList.get(i).getId());
//
//                List<SatisfactionResponse> satisfactionResponseList = satisfactionList.stream()
//                        .map(p->new SatisfactionResponse(p))
//                        .collect(Collectors.toList());
//
//            }
//
//        }

        return selectedAnswers;
    }

//    public List<QuestionResponse> getQuestionResponse(Long surveyId){
//        List<Question> questionList = questionRepository.findAllBySurvey_Id(surveyId);
//        List<QuestionResponse> questionAnswerResponseList = questionList.stream()
//                .map(p -> new QuestionResponse(p))
//                .collect(Collectors.toList());
//
//        return questionAnswerResponseList;
//    }


    /**
     * minseo
     */
    @Transactional
    public String postSatisfaction(Long surveyId,Long memberId,String satisfaction){
        Survey survey=surveyRepository.findById(surveyId).get();
        System.out.println("survey Id"+survey.getId());
        Long s=Long.parseLong(satisfaction);
        survey.setSatisfaction(s);

        return "만족도 등록 성공";
    }
}