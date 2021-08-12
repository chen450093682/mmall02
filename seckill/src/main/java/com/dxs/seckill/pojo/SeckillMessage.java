package com.dxs.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;

/**
 * @program: SecondKillMall
 * @description: 秒杀信息
 * @author: aaa
 * @create: 2021-05-31 19:35
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodsId;
}
