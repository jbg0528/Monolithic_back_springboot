package surfy.comfy.exception.post;

import surfy.comfy.config.BaseResponseStatus;
import surfy.comfy.exception.BusinessException;

public class CannotDeletePost extends BusinessException {
    public CannotDeletePost(){super(BaseResponseStatus.SURVEY_EXIST);}
}
