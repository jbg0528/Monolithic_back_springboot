//package surfy.comfy;
//
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import surfy.comfy.entity.*;
//import surfy.comfy.type.QuestionType;
//import surfy.comfy.type.SurveyType;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//import java.time.LocalDate;
//
//@Component
//@RequiredArgsConstructor
//public class InitDB {
//
//
//    private final InitService initService;
//    private static final Logger log= LoggerFactory.getLogger(InitDB.class);
//
//    @PostConstruct
//    public void init(){
//        initService.dbInit1();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService{
//
//        private final EntityManager em;
//
//        public void dbInit1(){
//
//            // set members
//            Member member1=createMember("a@gmail.com","aaa","aaapw");
//            em.persist(member1);
//
//            Member member2=createMember("b@gmail.com","bbb","bbbpw");
//            em.persist(member2);
//
//            Member member3=createMember("c@gmail.com","ccc","cccpw");
//            em.persist(member3);
//
//            Member member4=createMember("d@gmail.com","ddd","dddpw");
//            em.persist(member4);
//
//            Survey survey1=createSurvey("설문지1","설문지1입니다.",member1, SurveyType.finish);
//            em.persist(survey1);
//
//            Survey survey2=createSurvey("설문지2","설문지2입니다.",member2, SurveyType.finish);
//            em.persist(survey2);
//
//            Survey survey3=createSurvey("설문지3", "설문지3입니다.", member3, SurveyType.notFinish);
//            em.persist(survey3);
//
//            Survey survey4=createSurvey("설문지4","설문지4입니다.",member1, SurveyType.surveying);
//            em.persist(survey4);
//
//            Survey survey5=createSurvey("설문지5","설문지5입니다.",member1, SurveyType.finish);
//            em.persist(survey5);
//
//            Survey survey6=createSurvey("설문지6","설문지6입니다.",member1, SurveyType.notFinish);
//            em.persist(survey6);
//
//            // set questions
//            Question question1=createQuestion(survey1,QuestionType.객관식_단일,"객관식 단일 선택 질문입니다.");
//            em.persist(question1);
//
////            Question question2=createQuestion(survey1,QuestionType.객관식_중복,"객관식 중복 선택 질문입니다.");
////            em.persist(question2);
//
//            Question question3=createQuestion(survey1,QuestionType.객관식_그리드_단일,"객관식 그리드 단일 선택 질문입니다.");
//            em.persist(question3);
//
//            Question question7=createQuestion(survey1,QuestionType.만족도,"survey1-만족도 질문입니다.");
//            em.persist(question7);
//
//            Question question4=createQuestion(survey1,QuestionType.주관식,"주관식 질문입니다.");
//            em.persist(question4);
//
//            Question question5=createQuestion(survey2,QuestionType.파일,"파일 제출 질문입니다.");
//            em.persist(question5);
//
//            Question question6=createQuestion(survey2,QuestionType.선형배율,"선형배율 질문입니다.");
//            em.persist(question6);
//
//            Question question8=createQuestion(survey2,QuestionType.만족도,"survey2-만족도 질문입니다.");
//            em.persist(question8);
//
//            // set options
//            Option option1=createOption(question1,"설문지1-객관식 단일 선택-옵션1입니다.",survey1);
//            em.persist(option1);
//
//            Option option2=createOption(question1,"설문지1-객관식 단일 선택-옵션2입니다.",survey1);
//            em.persist(option2);
//
//            Option option3=createOption(question1,"설문지1-객관식 단일 선택-옵션3입니다.",survey1);
//            em.persist(option3);
//
//            Option option4=createOption(question3,"설문지1-객관식 그리드 단일 선택-옵션1입니다.- 매우 그렇다.",survey1);
//            em.persist(option4);
//
//            Option option5=createOption(question3,"설문지1-객관식 그리드 단일 선택-옵션2입니다. - 조금 그렇다.",survey1);
//            em.persist(option5);
//
//            Option option6=createOption(question3,"설문지1-객관식 그리드 단일 선택-옵션3입니다. - 그렇지 않다.",survey1);
//            em.persist(option6);
//
//            // set grids
//            Grid grid1=createGrid(question3,survey1,"주기적으로 새로운 친구를 만든다.");
//            em.persist(grid1);
//
//            Grid grid2=createGrid(question3,survey1,"자유 시간 중 상당 부분을 다양한 관심사를 탐구하는 데 할애한다.");
//            em.persist(grid2);
//
//            Grid grid3=createGrid(question3,survey1,"다른 사람이 울고 있는 모습을 보면 자신도 울고 싶어질 떄가 많다.");
//            em.persist(grid3);
//
//            // set linear
//            Linear linear=createLinear(question6,survey2,(long)5,"매우 그렇다","매우 그렇지 않다.");
//            em.persist(linear);
//            // set essay answer
//            Essay essay1=createEssay(member3,survey1, question4,"주관식 응답1입니다.");
//            em.persist(essay1);
//
//            Essay essay2=createEssay(member4,survey1, question4,"주관식 응답2입니다.");
//            em.persist(essay2);
//
//            Satisfaction satisfaction1 = createSatisfaction(member3, survey1, question7, (long) 90);
//            em.persist(satisfaction1);
//            Satisfaction satisfaction2 = createSatisfaction(member4, survey1, question7, (long) 80);
//            em.persist(satisfaction2);
//
//            // set answers
//            Answer answer1=createAnswer(member3,option1,QuestionType.객관식_단일,null,survey1,question1, null, null, null);
//            em.persist(answer1);
//
//            Answer answer2=createAnswer(member4,option3,QuestionType.객관식_단일,null,survey1,question1, null, null, null);
//            em.persist(answer2);
//
//            Answer answer3=createAnswer(member3,option4,QuestionType.객관식_그리드_단일,grid1,survey1,question3,null, null, null);
//            em.persist(answer3);
//
//            Answer answer4=createAnswer(member3,option4,QuestionType.객관식_그리드_단일,grid2,survey1,question3,null, null, null);
//            em.persist(answer4);
//
//            Answer answer5=createAnswer(member3,option6,QuestionType.객관식_그리드_단일,grid3,survey1,question3,null, null, null);
//            em.persist(answer5);
//
//            Answer answer6=createAnswer(member4,option4,QuestionType.객관식_그리드_단일,grid1,survey1,question3,null, null, null);
//            em.persist(answer6);
//
//            Answer answer7=createAnswer(member4,option5,QuestionType.객관식_그리드_단일,grid2,survey1,question3,null, null, null);
//            em.persist(answer7);
//
//            Answer answer8=createAnswer(member4,option6,QuestionType.객관식_그리드_단일,grid3,survey1,question3,null, null, null);
//            em.persist(answer8);
//
//            Answer answer9 = createAnswer(member3, null, QuestionType.주관식, null, survey1, question4, essay1, null, null);
//            em.persist(answer9);
//            Answer answer10 = createAnswer(member4, null, QuestionType.주관식, null, survey1, question4, essay2, null, null);
//            em.persist(answer10);
//
//            Answer answer11 = createAnswer(member3, null, QuestionType.만족도, null, survey1, question7, null, null, satisfaction1);
//            em.persist(answer11);
//
//            Answer answer12 = createAnswer(member4, null, QuestionType.만족도, null, survey1, question7, null, null, satisfaction2);
//            em.persist(answer12);
////            Answer answer6=createAnswer(member3,null,QuestionType.선형배율,null,survey2,question6,(long)2);
////            em.persist(answer6);
////
////            Answer answer7=createAnswer(member4,null,QuestionType.선형배율,null,survey2,question6,(long)5);
////            em.persist(answer7);
//
//            // set posts
//            Post post1=createPost(survey1,member1,"게시글1","게시글1입니다.",LocalDate.of(2022,9,14));
//            em.persist(post1);
//
//            Post post2=createPost(survey2,member2,"게시글2","게시글2입니다.",LocalDate.of(2022,10,9));
//            em.persist(post2);
//
//            // set bookmark - member1이 게시글2(post2)를 북마크함.
//            Bookmark bookmark1=createBookmark(member1,post2);
//            em.persist(bookmark1);
//        }
//
//
//        private Member createMember(String email, String username, String password){
//            Member member=new Member();
//            member.setEmail(email);
//            member.setName(username);
//            //member.setPassword(password);
//            log.info("member email: {}",member.getEmail());
//            return member;
//        }
//
//        private Survey createSurvey(String title,String contents,Member member, SurveyType surveyType){
//            Survey survey=new Survey();
//            survey.setTitle(title);
//            survey.setContents(contents);
//            survey.setMember(member);
//            survey.setStatus(surveyType);
//
//            return survey;
//        }
//
//        private Question createQuestion(Survey survey, QuestionType type,String contents){
//            Question question=new Question();
//            question.setSurvey(survey);
//            question.setQuestionType(type);
//            question.setContents(contents);
//
//            return question;
//        }
//
//        private Option createOption(Question question,String contents,Survey survey){
//            Option option=new Option();
//            option.setQuestion(question);
//            option.setContents(contents);
//            option.setSurvey(survey);
//
//            return option;
//        }
//
//        private Grid createGrid(Question question,Survey survey,String contents){
//            Grid grid=new Grid();
//            grid.setContents(contents);
//            grid.setQuestion(question);
//            grid.setSurvey(survey);
//
//            return grid;
//        }
//
//        private Linear createLinear(Question question,Survey survey,Long size,String start,String end){
//            Linear linear=new Linear();
//            linear.setQuestion(question);
//            linear.setSurvey(survey);
//            linear.setSize(size);
//            linear.setStart(start);
//            linear.setEnd(end);
//
//            return linear;
//        }
//
//        private Satisfaction createSatisfaction(Member member, Survey survey, Question question, Long percent){
//            Satisfaction satisfaction = new Satisfaction();
//            satisfaction.setMember(member);
//            satisfaction.setSurvey(survey);
//            satisfaction.setQuestion(question);
//            satisfaction.setPercent(percent);
//
//            return satisfaction;
//        }
//
//        private Answer createAnswer(Member member,Option option,QuestionType type,Grid grid,Survey survey,Question question,Essay essay, Long linear, Satisfaction satisfaction){
//            Answer answer=new Answer();
//            answer.setMember(member);
//            answer.setOption(option);
//            answer.setGrid(grid);
//            answer.setSurvey(survey);
//            answer.setQuestion(question);
//            answer.setEssay(essay);
//            answer.setLinear_answer(linear);
//            answer.setSatisfaction(satisfaction);
//
//            return answer;
//        }
//
//        private Essay createEssay(Member member,Survey survey, Question question,String contents){
//            Essay essay=new Essay();
//            essay.setMember(member);
//            essay.setSurvey(survey);
//            essay.setQuestion(question);
//            essay.setContents(contents);
//
//            return essay;
//        }
//
//        private Post createPost(Survey survey,Member member,String title,String contents,LocalDate uploadDate){
//            Post post=new Post();
//            post.setSurvey(survey);
//            post.setMember(member);
//            post.setTitle(title);
//            post.setContents(contents);
//            post.setUploadDate(uploadDate);
//            return post;
//        }
//
//        private Bookmark createBookmark(Member member,Post post){
//            Bookmark bookmark=new Bookmark();
//            bookmark.setMember(member);
//            bookmark.setPost(post);
//
//            return bookmark;
//        }
//    }
//}