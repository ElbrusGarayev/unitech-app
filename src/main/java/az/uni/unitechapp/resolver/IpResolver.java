package az.uni.unitechapp.resolver;

import az.uni.unitechapp.constant.HttpHeaderConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public final class IpResolver implements HandlerMethodArgumentResolver {

    private static final String ARGUMENT_NAME = "ip";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz.equals(String.class) && Objects.equals(methodParameter.getParameterName(), ARGUMENT_NAME);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        String remoteAddr = request.getHeader(HttpHeaderConstant.X_FORWARDED_FOR);
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }
        return remoteAddr;
    }

}
