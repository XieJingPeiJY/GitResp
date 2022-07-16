package org.linlinjava.litemall.wx.annotation.support;

import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.UserTokenManager;
import org.springframework.core.MethodParameter;//spring的帮助类
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String LOGIN_TOKEN_KEY = "X-Litemall-Token";  //这个是小程序前端请求头设定的  token的key>>X-Litemall-Token:jjssdwdawdawd(token)

    @Override//supportsParameter：用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument。
    public boolean supportsParameter(MethodParameter parameter) {
        System.out.println("经过此方法");
        return parameter.getParameterType().isAssignableFrom(Integer.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }   //上面的意思为判断parameter是否为Integer类型（MethodParameter spring自带的类，怎么用可以百度），
        //        和判断判断传进parameter参数是否带有LoginUser注解   //满足这两个条件则为true，如果是true就去调用下面的方法resolveArgument方法。
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,        //验证token
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {

//        return new Integer(1);
        String token = request.getHeader(LOGIN_TOKEN_KEY); // 根据key获得用户的token
        if (token == null || token.isEmpty()) {
            System.out.println("触发token为空");
            return null;
        }
        System.out.println("触发token有token");
        return UserTokenManager.getUserId(token);
    }
}
