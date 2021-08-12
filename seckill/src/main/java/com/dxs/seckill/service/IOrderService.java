package com.dxs.seckill.service;

import com.dxs.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxs.seckill.pojo.User;
import com.dxs.seckill.vo.GoodsVo;
import com.dxs.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aaa
 * @since 2021-05-16
 */
public interface IOrderService extends IService<Order> {

    /**
     * 生成秒杀订单
     * @param user
     * @param goodsVo
     * @return
     */
    Order seckill(User user, GoodsVo goodsVo);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);

    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /**
     * 校验秒杀地址
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(User user, Long goodsId, String path);

    /**
     * 校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
