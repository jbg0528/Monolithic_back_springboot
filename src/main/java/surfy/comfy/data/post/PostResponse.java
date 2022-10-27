package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Bookmark;
import surfy.comfy.entity.Post;

import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PostResponse {
    private String title;
    private String author;
    private Long postId;
    private LocalDate uploadDate;
    private Long authorId;

    public PostResponse(Post post){
        this.title=post.getTitle();
        this.author=post.getMember().getName();
        this.postId=post.getId();
        this.uploadDate=post.getUploadDate();
        this.authorId=post.getMember().getId();
//        this.type="post";
    }

    public PostResponse(Bookmark bookmark){
        this.title=bookmark.getPost().getTitle();
        this.author=bookmark.getPost().getMember().getName();
        this.postId= bookmark.getPost().getId();
        this.uploadDate=bookmark.getPost().getUploadDate();

//        this.type="bookmark";
    }
}
