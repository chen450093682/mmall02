package com.dxs.seckill.service;

import com.dxs.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxs.seckill.vo.LoginVo;
import com.dxs.seckill.vo.RespBean;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aaa
 * @since 2021-05-15
 */
public interface IUserService extends IService<User> {

    /**
     * 登录
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据Cookie获取用户
     * @param ticket
     * @return
     */
    User getUserByCookie(String ticket,HttpServletRequest request,HttpServletResponse response);

    /**
     * 修改密码
     * @param userTicket
     * @param password
     * @return
     */
    RespBean updatePassword(String userTicket ,String password,HttpServletRequest request, HttpServletResponse response);
}
