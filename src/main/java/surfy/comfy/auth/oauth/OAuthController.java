package surfy.comfy.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseException;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.token.TokenResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;
    Logger logger= LoggerFactory.getLogger(OAuthController.class);

    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
    //BaseResponse<String>
    public  BaseResponse<String> socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        String redirectUrl= oAuthService.request(socialLoginType);
        return new BaseResponse<>(redirectUrl);
    }

    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public BaseResponse<TokenResponse> callback (
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code)throws IOException, BaseException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        SocialLoginType socialLoginType= SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        logger.info("socialLoginType:{}",socialLoginType);
        TokenResponse tokenResponse=oAuthService.oAuthLogin(socialLoginType,code);

        return new BaseResponse<>(tokenResponse);
    }
}
