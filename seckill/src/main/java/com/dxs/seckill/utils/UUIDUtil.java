package com.dxs.seckill.utils;
import java.util.UUID;
/**
 * @program: SecondKillMall
 * @description: UUID工具类
 * @author: aaa
 * @create: 2021-05-15 16:58
 **/
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
