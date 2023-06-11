package com.wedding.serviceapi.common.resolver;

import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isAuthUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isAuthUserVo = parameter.getParameterType().equals(LoginUserVo.class);
        return isAuthUserAnnotation && isAuthUserVo;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        // TODO: 2023/06/10 boardsId가 없는 경우 처리해주는 로직 필요
        return new LoginUserVo(authUser.getUserId(), authUser.getUserName(), authUser.getBoardsId(), authUser.getUsers().getRole());
    }
}
