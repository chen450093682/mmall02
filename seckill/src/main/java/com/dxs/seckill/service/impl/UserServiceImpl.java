package com.dxs.seckill.service.impl;

import com.dxs.seckill.execption.GlobalException;
import com.dxs.seckill.pojo.User;
import com.dxs.seckill.mapper.UserMapper;
import com.dxs.seckill.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxs.seckill.utils.CookieUtil;
import com.dxs.seckill.utils.MD5Util;
import com.dxs.seckill.utils.UUIDUtil;
import com.dxs.seckill.vo.LoginVo;
import com.dxs.seckill.vo.RespBean;
import com.dxs.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aaa
 * @since 2021-05-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    protected RedisTemplate redisTemplate;

    /**
     * 登录
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
        //     return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        // }
        // if(!ValidatorUtil.isMobile(mobile)){
        //     return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        // }
        //是否为空判断
        User user = userMapper.selectById(mobile);
        if (user==null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //密码是否相同判断
        if (!MD5Util.fromPassToDBPass(password,user.getSalt()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成Cookie
        String ticket= UUIDUtil.uuid();
        // request.getSession().setAttribute(ticket,user);
        //将用户信息存入redis
        redisTemplate.opsForValue().set("user:"+ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    /**
     * 根据Cookie获取用户
     * @param ticket
     * @return
     */
    @Override
    public User getUserByCookie(String ticket,HttpServletRequest request,HttpServletResponse response) {
        if(StringUtils.isEmpty(ticket)){
            return null;
        }
        User user = (User)redisTemplate.opsForValue().get("user:" + ticket);

        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",ticket);
        }
        return user;
    }

    /**
     * 修改密码
     * @param userTicket
     * @param password
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket,String password,HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);

        if (user==null) {
            throw new GlobalException(RespBeanEnum.MOBIL_NOT_EXIST);
        }

        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if (result==1) {
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
