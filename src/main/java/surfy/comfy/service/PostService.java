package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.post.*;
import surfy.comfy.entity.*;
import surfy.comfy.exception.post.CannotDeletePost;
import surfy.comfy.exception.post.DeleteInvalidUser;
import surfy.comfy.repository.*;
import surfy.comfy.type.SurveyType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SatisfactionRepository satisfactionRepository;
    private final BookmarkService bookmarkService;
    private final Logger logger= LoggerFactory.getLogger(PostService.class);


    /**
     * minseo
     * 내가 작성한 게시글 조회
     * @param memberId
     * @return
     */
    @Transactional
    public List<PostResponse> getMyposts(Long memberId){

        // 내가 작성한 게시글들들
        List<Post> myPostList=postRepository.findAllByMember_Id(memberId);
        List<PostResponse> myPosts = myPostList.stream()
                .map(p -> new PostResponse(p))
                .collect(Collectors.toList());


        return myPosts;
    }

    /**
     * 모든 게시글 조회
     * @return allPosts
     */
    @Transactional
    public List<PostResponse> getAllPosts(){
        List<Post> allPostList=postRepository.findAll();
        List<PostResponse> allPosts=allPostList.stream()
                .map(p->new PostResponse(p))
                .collect(Collectors.toList());

        return allPosts;
    }

    /**
     * minseo
     * 게시글 info 조회
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional
    public GetPostResponse getPost(Long postId,String memberId){
        logger.info("[getPost] - memberId: {}",memberId);

        Post post=postRepository.findById(postId).get();
        List<Satisfaction> allSatisfactions=satisfactionRepository.findAllBySurvey_Id(post.getSurvey().getId());
        Long total=0L;

        for(Satisfaction s:allSatisfactions){
            total+=s.getPercent();
        }
        int average= total.intValue()/allSatisfactions.size();
        System.out.println("satisfaction average: "+average);
        Boolean isBookmarked=false;
        Boolean member_case=false;


        if(memberId.equals("null")){ // 비회원
            member_case=false;
        }
        else{ // 회원
            Long member_id=Long.parseLong(memberId);
            member_case=true;
            Bookmark bookmark=bookmarkRepository.findByMember_IdAndPost_Id(member_id,postId);
            if(bookmark==null){
                isBookmarked=false;
            }
            else{
                isBookmarked=true;
            }
        }

        GetPostResponse response=new GetPostResponse(post,isBookmarked,member_case,post.getSurvey().getSatisfaction(),average);

        return response;
    }

    @Transactional
    public String createPost(CreatePostRequest request){
        Post post=new Post();
        post.setTitle(request.getTitle());
        post.setContents(request.getContents());
        post.setSurvey(surveyRepository.findById(request.getSurveyId()).get());
        post.setMember(memberRepository.findById(request.getAuthor()).get());

        postRepository.save(post);

        return "게시글 생성 완료";

    }

//    @Transactional
//    public DeletePostResponse deletePost(Long postId, String memberId){
//        Post post=postRepository.findById(postId).get();
//        Optional<Survey> survey=surveyRepository.findById(post.getSurvey().getId());
//        if(!survey.isPresent()){
//            throw new CannotDeletePost();
//        }
//        if(post.getMember().getId()!=Long.parseLong(memberId)){
//            logger.info("throw exception");
//            throw new DeleteInvalidUser();
//        }
//
//        // 해당 게시글을 북마크한 사람들의 북마크 삭제
//        List<Bookmark> bookmarks=bookmarkRepository.findAllByPost_Id(postId);
//        for(Bookmark bookmark:bookmarks){
//            bookmarkRepository.delete(bookmark);
//        }
//
//        postRepository.delete(post);
//
//
//        return new DeletePostResponse(postId,Long.parseLong(memberId));
//    }

    /**
     * minseo
     * 게시글 삭제
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional
    public DeletePostResponse deletePost(Long postId, String memberId){
        Post post=postRepository.findById(postId).get();

        // 해당 게시글을 북마크한 사람들의 북마크 삭제
        List<Bookmark> bookmarks=bookmarkRepository.findAllByPost_Id(postId);
        for(Bookmark bookmark:bookmarks){
            bookmarkRepository.delete(bookmark);
        }

        postRepository.delete(post);

        return new DeletePostResponse(postId,Long.parseLong(memberId));
    }

    /**
     * 게시글 검색
     * @param title
     * @return
     */
    @Transactional
    public List<PostResponse> searchPost(String title){
        List<Post> SearchList = postRepository.findByTitleContaining(title);
        List<PostResponse> search=SearchList.stream()
                .map(p -> new PostResponse(p))
                .collect(Collectors.toList());

        System.out.println("service:"+search);
        return search;
    }

    /**
     * 내 설문지 조회
     * @param memberId
     * @return
     */
    @Transactional
    public List<MySurveyResponse> getMySurvey(Long memberId){
        List<Survey> mySurveyList = surveyRepository.findAllByMember_Id(memberId);
        List<MySurveyResponse> surveyList=mySurveyList.stream()
                .map(p -> new MySurveyResponse(p))
                .collect(Collectors.toList());
        System.out.println("SurveyList: "+surveyList);
        return surveyList;
    }

    /**
     * 설문 완료된 내 설문지 조회
     * @param memberId
     * @param status
     * @return
     */
    @Transactional
    public List<MySurveyResponse> getMySurvey(Long memberId, SurveyType status){
        List<Survey> mySurveyList = surveyRepository.findAllByMember_IdAndStatus(memberId,SurveyType.finish);
        List<MySurveyResponse> surveyList=mySurveyList.stream()
                .map(p -> new MySurveyResponse(p))
                .collect(Collectors.toList());
        System.out.println("SurveyList: "+surveyList);
        return surveyList;
    }

    /**
     * 게시글 등록
     * @param request
     * @return
     */
    @Transactional
    public RequestPost registerPost(RequestPost request){
        Post post=new Post();
        Member member=memberRepository.findById(request.getMemberId()).get();
        Survey survey=surveyRepository.findById(request.getSurveyId()).get();
        post.setTitle(request.getTitle());
        post.setContents(request.getContents());
        post.setMember(member);
        post.setSurvey(survey);
        post.setUploadDate(LocalDate.now());
        return new RequestPost(postRepository.saveAndFlush(post));
    }
}
