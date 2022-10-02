package surfy.comfy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import surfy.comfy.auth.JwtTokenInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;



    public void addInterceptors(InterceptorRegistry registry) {
        // TODO: jwt interceptor addPathPatterns 수정 필요
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/member/**")
                .addPathPatterns("/bookmark/**")
                .addPathPatterns("/folder/**")
                .addPathPatterns("/search")
                .addPathPatterns("/team/**")
                .addPathPatterns("/trash/**");


    }

    @Override
    public void addCorsMappings(CorsRegistry registry){
        // TODO: allowedOrigins-> 우리 서버 ip로 수정 필요 ex)http://192.126.32.2:3000
        registry.addMapping("/**")
                .allowedOrigins("*");
    }
}