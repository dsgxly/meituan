package com.itdr.controller.common.exception;

import com.itdr.common.ResponseCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
  @Component 将类交给spring的bean进行管理  普通bean
  @Service  业务逻辑层
  @Repository  Dao层
  @Controller   控制层
  作为spring的bean进行管理
 全局异常处理类
*/

@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        e.printStackTrace();
        //将modelAndView转成json格式
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        modelAndView.addObject("status",ResponseCode.ERROR);
        modelAndView.addObject("msg","接口调用出错");
        modelAndView.addObject("data",e.toString());

        return modelAndView;
    }
}
