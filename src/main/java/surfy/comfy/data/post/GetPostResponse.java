package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Post;

@Data
@AllArgsConstructor @NoArgsConstructor
public class GetPostResponse {
    private Long postId;
    private String title;
    private String contents;

    //private String thumbnail;
    private Long surveyId;
    private String surveyTitle;
    private String authorName;
    private Long authorId;

    public GetPostResponse(Post post){
        this.postId=post.getId();
        this.title=post.getTitle();
        this.contents=post.getContents();
        //this.thumbnail=post.getThumbnail();
        this.surveyId=post.getSurvey().getId();
        this.surveyTitle=post.getSurvey().getTitle();
        this.authorName=post.getMember().getName();
        this.authorId=post.getMember().getId();
    }

}
