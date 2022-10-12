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
import surfy.comfy.repository.BookmarkRepository;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.PostRepository;
import surfy.comfy.repository.SurveyRepository;
import surfy.comfy.type.SurveyType;

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
    private final BookmarkService bookmarkService;
    private final Logger logger= LoggerFactory.getLogger(PostService.class);

    @Transactional
    public List<PostResponse> getMyposts(Long memberId){

        // 내가 작성한 게시글들들
        List<Post> myPostList=postRepository.findAllByMember_Id(memberId);
        List<PostResponse> myPosts = myPostList.stream()
                .map(p -> new PostResponse(p))
                .collect(Collectors.toList());


        return myPosts;
    }

    @Transactional
    public List<PostResponse> getAllPosts(){
        List<Post> allPostList=postRepository.findAll();
        List<PostResponse> allPosts=allPostList.stream()
                .map(p->new PostResponse(p))
                .collect(Collectors.toList());

        return allPosts;
    }

    @Transactional
    public GetPostResponse getPost(Long postId,String memberId){
        logger.info("[getPost] - memberId: {}",memberId);

        Post post=postRepository.findById(postId).get();
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

        GetPostResponse response=new GetPostResponse(post,isBookmarked,member_case);

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
    @Transactional
    public List<PostResponse> searchPost(String title){
        List<Post> SearchList = postRepository.findByTitleContaining(title);
        List<PostResponse> search=SearchList.stream()
                .map(p -> new PostResponse(p))
                .collect(Collectors.toList());

        System.out.println("service:"+search);
        return search;
    }

    @Transactional
    public List<MySurveyResponse> getMySurvey(Long memberId){
        List<Survey> mySurveyList = surveyRepository.findAllByMember_Id(memberId);
        List<MySurveyResponse> surveyList=mySurveyList.stream()
                .map(p -> new MySurveyResponse(p))
                .collect(Collectors.toList());
        System.out.println("SurveyList: "+surveyList);
        return surveyList;
    }

    @Transactional
    public List<MySurveyResponse> getMySurvey(Long memberId, SurveyType status){
        List<Survey> mySurveyList = surveyRepository.findAllByMember_IdAndStatus(memberId,SurveyType.finish);
        List<MySurveyResponse> surveyList=mySurveyList.stream()
                .map(p -> new MySurveyResponse(p))
                .collect(Collectors.toList());
        System.out.println("SurveyList: "+surveyList);
        return surveyList;
    }

    @Transactional
    public RequestPost registerPost(RequestPost request){
        Post post=new Post();
        Member member=memberRepository.findById(request.getMemberId()).get();
        Survey survey=surveyRepository.findById(request.getSurveyId()).get();
        post.setTitle(request.getTitle());
        post.setContents(request.getContents());
        post.setMember(member);
        post.setSurvey(survey);
        return new RequestPost(postRepository.saveAndFlush(post));
    }
}
