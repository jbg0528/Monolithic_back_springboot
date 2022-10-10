package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.bookmark.PostBookmarkResponse;
import surfy.comfy.data.post.*;
import surfy.comfy.entity.Post;
import surfy.comfy.service.BookmarkService;
import surfy.comfy.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BookmarkService bookmarkService;

    private final Logger logger= LoggerFactory.getLogger(PostController.class);

    /**
     * 마이 페이지 조회
     * @param memberId
     * @return MyPageResponse
     */
    @GetMapping("/myPage/{memberId}")
    public BaseResponse<MyPageResponse> myPage(@PathVariable(name="memberId")Long memberId) {
        logger.info("mypage - memberId: {}",memberId);
        List<PostResponse> myPostList = postService.getMyposts(memberId);
        List<PostResponse> bookmarkList = bookmarkService.getBookmarks(memberId);
        logger.info("bookmarkList: {}",bookmarkList.size());
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
     * @return memberId
     */
    @GetMapping("/post/{postId}/{memberId}")
    public BaseResponse<GetPostResponse> viewPost(@PathVariable(name="postId")Long postId,@PathVariable(name="memberId")String memberId){
        logger.info("[viewPost] - request: {}",memberId);
        GetPostResponse getPostResponse=postService.getPost(postId, memberId);

        return new BaseResponse<>(getPostResponse);
    }



    /**
     * 게시글 삭제
     * @param postId
     * @param memberId
     * @return
     */
    @DeleteMapping("/post/{postId}/{memberId}")
    public BaseResponse<DeletePostResponse> deletePost(@PathVariable(name="postId")Long postId,@PathVariable(name="memberId")String memberId){
        //logger.info("[deletePost]: {}",postService.deletePost(postId,memberId));
        DeletePostResponse response= postService.deletePost(postId,memberId);
        logger.info("[deletePost]: {}",response);
        return new BaseResponse<>(response);
    }

    /**
     * 게시글 제목으로 검색
     * @param title
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<PostResponse>> Search(String title){
        List<PostResponse> SearchList = postService.searchPost(title);
        System.out.println("title:"+ title);
        System.out.println("search:"+ SearchList);
        return new BaseResponse<>(SearchList);

    }


}
