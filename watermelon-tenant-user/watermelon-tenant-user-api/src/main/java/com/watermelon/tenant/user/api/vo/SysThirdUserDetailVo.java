package com.watermelon.tenant.user.api.vo;


import com.watermelon.common.core.enums.AccountPlatform;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方服务账号详情dto
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Data
public class SysThirdUserDetailVo implements Serializable {

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * id
     */
    private Long id;
    /**
     * 第三方平台唯一id
     */
    private String uniqueId;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 平台类型(WX:微信；QQ:QQ)
     */
    private AccountPlatform type;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户id
     */
    private Long userId;
}
