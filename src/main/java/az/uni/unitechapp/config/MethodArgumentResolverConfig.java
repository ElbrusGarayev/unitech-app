package az.uni.unitechapp.config;

import az.uni.unitechapp.resolver.IpResolver;
import az.uni.unitechapp.resolver.UserClaimsResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MethodArgumentResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userClaimsResolver());
        argumentResolvers.add(ipResolver());
    }

    @Bean
    public UserClaimsResolver userClaimsResolver() {
        return new UserClaimsResolver();
    }

    @Bean
    public IpResolver ipResolver() {
        return new IpResolver();
    }

}
