package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.post.CreatePostRequest;
import surfy.comfy.data.post.CreatePostResponse;
import surfy.comfy.data.post.GetPostResponse;
import surfy.comfy.data.post.PostResponse;
import surfy.comfy.entity.Bookmark;
import surfy.comfy.entity.Post;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.PostRepository;
import surfy.comfy.repository.SurveyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final BookmarkService bookmarkService;

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
    public GetPostResponse getPost(Long postId){
        Post post=postRepository.findById(postId).get();

        GetPostResponse response=new GetPostResponse(post);

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
}
