package com.dxs.seckill.service;

import com.dxs.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxs.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aaa
 * @since 2021-05-16
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodVo();

    /**
     * 根据id获取详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodVoByGoodsId(Long goodsId);
}
