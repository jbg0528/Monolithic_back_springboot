package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.post.MyPageResponse;
import surfy.comfy.data.post.PostResponse;
import surfy.comfy.service.BookmarkService;
import surfy.comfy.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BookmarkService bookmarkService;

    @GetMapping("/myPage/{memberId}")
    public BaseResponse<MyPageResponse> myPage(@PathVariable(name="memberId")Long memberId){
        List<PostResponse> myPostList=postService.getMyposts(memberId);
        List<PostResponse> bookmarkList=bookmarkService.getBookmarks(memberId);

        MyPageResponse result=new MyPageResponse();
        result.setBookmarks(bookmarkList);
        result.setMyposts(myPostList);
//        List<PostResponse> result=new ArrayList<>();
//        result.addAll(myPostList);
//        result.addAll(bookmarkList);

        return new BaseResponse<>(result);
    }
}
