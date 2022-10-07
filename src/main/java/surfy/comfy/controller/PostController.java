package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.post.*;
import surfy.comfy.service.BookmarkService;
import surfy.comfy.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BookmarkService bookmarkService;

    /**
     * 마이 페이지 조회
     * @param memberId
     * @return MyPageResponse
     */
    @GetMapping("/myPage/{memberId}")
    public BaseResponse<MyPageResponse> myPage(@PathVariable(name="memberId")Long memberId) {
        List<PostResponse> myPostList = postService.getMyposts(memberId);
        List<PostResponse> bookmarkList = bookmarkService.getBookmarks(memberId);

        MyPageResponse result = new MyPageResponse();
        result.setBookmarks(bookmarkList);
        result.setMyposts(myPostList);

        return new BaseResponse<>(result);
    }


    /**
     * 커뮤니티 조회
     * @return List<PostResponse>
     */
    @GetMapping("/community")
    public BaseResponse<List<PostResponse>> community(){
        List<PostResponse> posts=postService.getAllPosts();

        return new BaseResponse<>(posts);
    }

    /**
     * 게시글 조회
     * @param postId
     * @return GetPostResponse
     */
    @GetMapping("/post/{postId}")
    public BaseResponse<GetPostResponse> viewPost(@PathVariable(name="postId")Long postId){
        GetPostResponse getPostResponse=postService.getPost(postId);

        return new BaseResponse<>(getPostResponse);
    }

    /**
     * 게시글 생성
     * @param createPostRequest
     * @return String
     */
    @PostMapping("/post")
    public BaseResponse<String> createPost(@RequestBody CreatePostRequest createPostRequest){
        String response=postService.createPost(createPostRequest);

        return new BaseResponse<>(response);
    }


}
