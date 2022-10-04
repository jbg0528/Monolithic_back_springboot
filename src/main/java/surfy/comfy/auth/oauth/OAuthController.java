package surfy.comfy.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import surfy.comfy.config.BaseException;
import surfy.comfy.config.BaseResponse;
import surfy.comfy.data.token.TokenResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;
    Logger logger= LoggerFactory.getLogger(OAuthController.class);
    private final HttpServletResponse response;

    @GetMapping("/login/{socialLoginType}") //GOOGLE이 들어올 것이다.
    //BaseResponse<String>
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        String redirectUrl= oAuthService.request(socialLoginType);
        response.sendRedirect(redirectUrl);

        //return new BaseResponse<>(redirectUrl);
    }

//    @GetMapping(value = "/auth/{socialLoginType}/callback")
//    public BaseResponse<TokenResponse> callback (
//            @PathVariable(name = "socialLoginType") String socialLoginPath,
//            @RequestParam(name = "code") String code)throws IOException, BaseException {
//        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
//        SocialLoginType socialLoginType= SocialLoginType.valueOf(socialLoginPath.toUpperCase());
//        logger.info("socialLoginType:{}",socialLoginType);
//        TokenResponse tokenResponse=oAuthService.oAuthLogin(socialLoginType,code);
//
//        return new BaseResponse<>(tokenResponse);
//    }

//    @GetMapping(value="/login/{socialLoginType}")
    @GetMapping(value="/login/google/{accessToken}")
    public BaseResponse<TokenResponse> login(@PathVariable(name="accessToken") String accessToken) throws IOException {
//    public BaseResponse<TokenResponse> login(@PathVariable(name = "socialLoginType") String socialLoginPath, @RequestParam(name="accessToken") String accessToken) throws IOException {
        //logger.info("[login] socialLoginType: {}",socialLoginPath);
        logger.info("[login] accessToken: {}",accessToken);
        SocialLoginType socialLoginType= SocialLoginType.valueOf("google".toUpperCase());
        TokenResponse tokenResponse=oAuthService.oAuthLogin(socialLoginType,accessToken);
        logger.info("tokenResponse: {}",tokenResponse);

        return new BaseResponse<>(tokenResponse);
    }

//    @GetMapping("/auth/google/{code}")
//    public BaseResponse<TokenResponse> login2 (
//            @PathVariable(name = "code") String code)throws IOException, BaseException {
//        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
//        SocialLoginType socialLoginType= SocialLoginType.valueOf(socialLoginPath.toUpperCase());
//        logger.info("socialLoginType:{}",socialLoginType);
//        TokenResponse tokenResponse=oAuthService.oAuthLogin(socialLoginType,code);
//
//        return new BaseResponse<>(tokenResponse);
//    }
}
