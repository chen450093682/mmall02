package com.dxs.seckill.vo;

import com.dxs.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: SecondKillMall
 * @description: 订单详情
 * @author: aaa
 * @create: 2021-05-26 20:07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {

    private Order order;
    private GoodsVo goodsVo;
}
