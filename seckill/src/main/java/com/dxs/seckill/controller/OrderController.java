package com.dxs.seckill.controller;


import com.dxs.seckill.pojo.User;
import com.dxs.seckill.service.IOrderService;
import com.dxs.seckill.vo.OrderDetailVo;
import com.dxs.seckill.vo.RespBean;
import com.dxs.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author aaa
 * @since 2021-05-16
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     *  订单详情
     * @param user
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        OrderDetailVo detailVo=orderService.detail(orderId);
        System.out.println(detailVo);
        return RespBean.success(detailVo);
    }

}
