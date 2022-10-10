package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Bookmark;
import surfy.comfy.entity.Post;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PostResponse {
    private String title;
    private String author;
    private Long postId;
//    private String type; // 북마크 or 커뮤니티 글(내가 작성한 글 + 커뮤니티 글)
    public PostResponse(Post post){
        this.title=post.getTitle();
        this.author=post.getMember().getName();
        this.postId=post.getId();
//        this.type="post";
    }

    public PostResponse(Bookmark bookmark){
        this.title=bookmark.getPost().getTitle();
        this.author=bookmark.getPost().getMember().getName();
        this.postId= bookmark.getPost().getId();
//        this.type="bookmark";
    }
}
