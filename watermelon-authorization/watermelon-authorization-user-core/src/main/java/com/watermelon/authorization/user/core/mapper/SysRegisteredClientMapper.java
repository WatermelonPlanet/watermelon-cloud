package com.watermelon.authorization.user.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.authorization.user.core.mapper.entity.SysRegisteredClientDo;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Oauth2RegisteredClient)表数据库访问层
 * @author byh
 * @since 2023-09-14
 */
@Mapper
public interface SysRegisteredClientMapper extends BaseMapper<SysRegisteredClientDo> {

}

