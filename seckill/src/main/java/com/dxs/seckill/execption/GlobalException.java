package com.dxs.seckill.execption;

import com.dxs.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: SecondKillMall
 * @description: 全局异常
 * @author: aaa
 * @create: 2021-05-15 16:39
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;
}
