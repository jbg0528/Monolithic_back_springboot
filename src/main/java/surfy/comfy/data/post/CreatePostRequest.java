package surfy.comfy.data.post;

import lombok.Data;

@Data
public class CreatePostRequest {

    private String title;
    private String contents;
    private Long surveyId;
    private Long author;
}
