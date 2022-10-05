package surfy.comfy.auth.oauth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import surfy.comfy.auth.JwtTokenProvider;
import surfy.comfy.data.token.TokenResponse;
import surfy.comfy.entity.Member;
import surfy.comfy.entity.Token;
import surfy.comfy.exception.token.RefreshTokenNotFound;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    Logger logger= LoggerFactory.getLogger(OAuthService.class);

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String request(SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL = googleOauth.getOauthRedirectURL();
                logger.info("redirectURL: {}",redirectURL);
            }
            break;
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }
        return redirectURL;
        //response.sendRedirect(redirectURL);
    }

    public TokenResponse oAuthLogin(SocialLoginType socialLoginType, String accessToken) throws IOException {
        switch (socialLoginType) {
            case GOOGLE: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                //ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                //GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                //ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(accessToken);
                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);
                logger.info("googleUser: {}",googleUser);
                String user_id = googleUser.getEmail();
                logger.info("userId: {}", user_id);

                String jwtAccessToken=jwtTokenProvider.createAccessToken(googleUser.getEmail());
                String jwtRefreshToken=jwtTokenProvider.createRefreshToken(googleUser.getEmail());

                if(!isJoinedUser(googleUser)){
                    signUp(googleUser,jwtRefreshToken);
                }

                Member member = memberRepository.findByEmail(googleUser.getEmail()).orElseThrow(IllegalArgumentException::new);
                TokenResponse tokenResponse=new TokenResponse(jwtAccessToken,jwtRefreshToken,member.getId(),member.getName(),member.getEmail());

                return tokenResponse;
                //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.
//                int user_num=accountProvider.getUserNum(user_id);
//
//                if(user_num!=0){
//                    //서버에 user가 존재하면 앞으로 회원 인가 처리를 위한 jwtToken을 발급한다.
//                    String jwtToken=jwtService.createJwt(user_num,user_id);
//                    //액세스 토큰과 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
//                    GetSocialOAuthRes getSocialOAuthRes=new GetSocialOAuthRes(jwtToken,user_num,oAuthToken.getAccess_token(),oAuthToken.getToken_type());
//                    return getSocialOAuthRes;
//                }
//                else {
//                    throw new BaseException(BaseResponseStatus.ACCOUNT_DOESNT_EXISTS);
//                }

            }
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
    }

    private boolean isJoinedUser(GoogleUser googleUser) {
        Optional<Member> member = memberRepository.findByEmail(googleUser.getEmail());
        logger.info("Joined User: {}", member);
        return member.isPresent();
    }

    private void signUp(GoogleUser googleUser,String refreshToken) {

        Member member = googleUser.toUserSignUp();
        memberRepository.save(member);

        Token token=new Token();
        token.setMember(member);
        token.setRefreshToken(refreshToken);
        tokenRepository.save(token);


    }

    // refresh token으로 access token 재발급
    public TokenResponse issueAccessToken(HttpServletRequest request){
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        logger.info("accessToken = {}",accessToken);
        logger.info("refreshToken = {}",refreshToken);

        //accessToken이 만료됐고 refreshToken이 맞으면 accessToken을 새로 발급(refreshToken의 내용을 통해서)
        if(!jwtTokenProvider.isValidAccessToken(accessToken)){  //클라이언트에서 토큰 재발급 api로의 요청을 확정해주면 이 조건문은 필요없다.
            logger.info("Expired Access Token");
            if(jwtTokenProvider.isValidRefreshToken(refreshToken)){     //들어온 Refresh 토큰이 유효한지
                logger.info("Valid Refresh Token");
                Claims claimsToken = jwtTokenProvider.getClaimsToken(refreshToken);
                String email = (String)claimsToken.get("email");
                Optional<Member> member = memberRepository.findByEmail(email);
                String tokenFromDB = tokenRepository.findByMemberId(member.get().getId()).get().getRefreshToken();
                logger.info("refresh token from DB: {}",tokenFromDB);
                if(refreshToken.equals(tokenFromDB)) {   //DB의 refresh토큰과 지금들어온 토큰이 같은지 확인
                    logger.info("reissue access token");
                    accessToken = jwtTokenProvider.createAccessToken(email);

                }
                else{
                    //DB의 Refresh토큰과 들어온 Refresh토큰이 다르면 중간에 변조된 것임
                    logger.error("Refresh Token Tampered");
                    throw new RefreshTokenNotFound();
                }
            }
            else{
                //입력으로 들어온 Refresh 토큰이 유효하지 않음
                logger.error("Invalid Refresh Token");
                throw new RefreshTokenNotFound();
            }
        }
        return TokenResponse.builder()
                .ACCESS_TOKEN(accessToken)
                .REFRESH_TOKEN(refreshToken)
                .build();
    }

}
