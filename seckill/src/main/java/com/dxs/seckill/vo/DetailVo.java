package com.dxs.seckill.vo;

import com.dxs.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: SecondKillMall
 * @description: 详情返回对象
 * @author: aaa
 * @create: 2021-05-22 16:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private User user;
    private GoodsVo goodsVo;
    private int seckillStatus;
    private int remainSeconds;
}
