package org.starlightfinancial.deductiongateway.common;

import com.alibaba.fastjson.JSON;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sili.chen on 2017/7/31
 */

public class SameUrlDataInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SameUrlData annotation = method.getAnnotation(SameUrlData.class);
            if (annotation != null) {
                if (repeatDataValidator(request)) {//如果重复相同数据
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(JSON.toJSONString("请勿重复提交数据"));
                    return false;
                } else {
                    return true;
                }

            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * 验证同一个url数据是否相同提交  ,相同返回true
     *
     * @param httpServletRequest
     * @return
     */
    public boolean repeatDataValidator(HttpServletRequest httpServletRequest) {
        String params = JSON.toJSONString(httpServletRequest.getParameterMap());
        String url = httpServletRequest.getRequestURI();
        Map<String, String> map = new HashMap<>();
        map.put(url, params);
        String nowUrlParams = map.toString();//

        Object preUrlParams = httpServletRequest.getSession().getAttribute("repeatData");
        if (preUrlParams == null)//如果上一个数据为null,表示还没有访问页面
        {
            httpServletRequest.getSession().setAttribute("repeatData", nowUrlParams);
            return false;
        } else//否则，已经访问过页面
        {
            if (preUrlParams.toString().equals(nowUrlParams))//如果上次url+数据和本次url+数据相同，则表示重复添加数据
            {

                return true;
            } else//如果上次 url+数据 和本次url加数据不同，则不是重复提交
            {
                httpServletRequest.getSession().setAttribute("repeatData", nowUrlParams);
                return false;
            }

        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
