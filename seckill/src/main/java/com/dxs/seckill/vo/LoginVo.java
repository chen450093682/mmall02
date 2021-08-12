package com.dxs.seckill.vo;

import com.dxs.seckill.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @program: SecondKillMall
 * @description: 登陆参数
 * @author: aaa
 * @create: 2021-05-15 15:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;
}
