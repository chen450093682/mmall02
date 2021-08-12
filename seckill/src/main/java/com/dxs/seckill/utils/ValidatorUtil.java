package com.dxs.seckill.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: SecondKillMall
 * @description: 手机号校验
 * @author: aaa
 * @create: 2021-05-15 15:42
 **/
public class ValidatorUtil {
    private static final Pattern mobile_patter=Pattern.compile("[1]([3-9])[0-9]{9}$");

    public static boolean isMobile(String mobile){
        if (StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = mobile_patter.matcher(mobile);
        return matcher.matches();
    }
}
