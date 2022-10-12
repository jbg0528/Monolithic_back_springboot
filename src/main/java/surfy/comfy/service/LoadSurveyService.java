package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import surfy.comfy.type.QuestionType;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadSurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final GridRepository gridRepository;
    private final EssayRepository essayRepository;

    @SneakyThrows
    @Transactional
    public String getSurveydata(Long surveyId){
        Survey RespondentSurvey= surveyRepository.findSurveysById(surveyId);
        String intro0="intro0:\""+RespondentSurvey.getTitle()+"\"";
        String intro1="intro1:\""+RespondentSurvey.getContents()+"\"";
        String ques_list="ques_list:[";

        List<Question> Ques_list=questionRepository.findAllBySurvey_Id(surveyId);

        for(int i=0;i<Ques_list.size();i++){
            Question question=Ques_list.get(i);
            if(question.getQuestionType()==QuestionType.만족도){
                continue;
            }

            String ques_item="{id:"+i+",ques:\""+question.getContents()+"\"";
            if(question.getQuestionType()==QuestionType.객관식_단일 || question.getQuestionType()==QuestionType.객관식_중복){
                List<Option> Opt_list=optionRepository.findAllByQuestion_Id(question.getId());
                String type="type:{id:1,name:\"객관식\",";
                String ans_list="ans_list:[";
                for(int k=0;k<Opt_list.size();k++){
                    Option option=Opt_list.get(k);
                    String opt_item="{id:"+k+",text:\""+option.getContents()+"\",choice_value:0}";
                    ans_list=ans_list+opt_item+",";
                }
                ans_list=ans_list+"]";
                if(question.getQuestionType()==QuestionType.객관식_단일){
                    type=type+"que_type:{"+ans_list+",choice_type:0}}}";
                }
                else{
                    type=type+"que_type:{"+ans_list+",choice_type:1}}}";
                }
                ques_item=ques_item+","+type;
            }
            else if(question.getQuestionType()==QuestionType.객관식_그리드_단일 || question.getQuestionType()==QuestionType.객관식_그리드_중복){
                List<Option> Opt_list=optionRepository.findAllByQuestion_Id(question.getId());
                List<Grid> Grid_list=gridRepository.findAllByQuestion_Id(question.getId());
                String type="type:{id:2,name:\"객관식 Grid\",";
                String choice_list="choice_list:[";
                for(int k=0;k<Opt_list.size();k++){
                    Option option=Opt_list.get(k);
                    String opt_item="{id:"+k+",text:\""+option.getContents()+"\"}";
                    choice_list=choice_list+opt_item+",";
                }
                choice_list=choice_list+"]";

                String ans_list="ans_list:[";
                for(int k=0;k<Grid_list.size();k++){
                    Grid grid=Grid_list.get(k);
                    String opt_item="{id:"+k+",text:\""+grid.getContents()+"\",choice_value:0}";
                    ans_list=ans_list+opt_item+",";
                }
                ans_list=ans_list+"]";

                if(question.getQuestionType()==QuestionType.객관식_그리드_단일){
                    type=type+"que_type:{"+ans_list+","+choice_list+",choice_type:0}}}";
                }
                else{
                    type=type+"que_type:{"+ans_list+","+choice_list+",choice_type:1}}}";
                }
                ques_item=ques_item+","+type;
            }
            else if(question.getQuestionType()==QuestionType.주관식){
                List<Essay> Essay_list=essayRepository.findAllByQuestion_Id(question.getId());

                String type="type:{id:3,name:\"주관식\",ans:\"\"}";
                ques_item=ques_item+","+type+"}";
            }
            ques_list=ques_list+ques_item+",";
        }
        ques_list=ques_list+"]";
        String result="{"+intro0+","+intro1+","+ques_list+"}";
        return result;
    }
}
