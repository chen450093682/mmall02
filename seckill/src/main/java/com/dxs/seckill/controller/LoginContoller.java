package com.dxs.seckill.controller;

import com.dxs.seckill.service.IUserService;
import com.dxs.seckill.utils.ValidatorUtil;
import com.dxs.seckill.vo.LoginVo;
import com.dxs.seckill.vo.RespBean;
import com.dxs.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @program: SecondKillMall
 * @description: 登录
 * @author: aaa
 * @create: 2021-05-15 10:40
 **/
@Controller
@Slf4j
@RequestMapping("/login")
public class LoginContoller {

    @Autowired
    private IUserService userService;

    /**
     * 跳转登界面
     * @return
     */
    @RequestMapping("toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 登陆功能
     * @param loginVo
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        // log.info("{}",loginVo);
        return userService.doLogin(loginVo,request,response);
    }
}
