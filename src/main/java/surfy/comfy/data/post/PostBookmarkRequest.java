package surfy.comfy.data.post;

import lombok.Data;

@Data
public class PostBookmarkRequest {
    private Long memberId;
    private Long postId;
}
