package com.dxs.seckill.service;

import com.dxs.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxs.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aaa
 * @since 2021-05-16
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return
     */
    Long getResult(User user, Long goodsId);
}
