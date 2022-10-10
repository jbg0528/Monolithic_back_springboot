package surfy.comfy.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.service.BookmarkService;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final Logger logger= LoggerFactory.getLogger(BookmarkController.class);

    /**
     * 즐겨찾기 추가
     * @param postId
     * @param memberId
     * @return
     */
    @PostMapping("/bookmark/{postId}/{memberId}")
    public BaseResponse<String> addBookmark(@PathVariable(name="postId")Long postId, @PathVariable(name="memberId")Long memberId){
        String response=bookmarkService.addBookmark(postId,memberId);

        return new BaseResponse<>(response);
    }
    @DeleteMapping("/bookmark/{postId}/{memberId}")
    public BaseResponse<String> deleteBookmark(@PathVariable(name="postId") Long postId, @PathVariable(name="memberId") Long memberId){
        logger.info("[deleteBookmark]");
        String response=bookmarkService.deleteBookmark(postId,memberId);

        return new BaseResponse<>(response);
    }


}
