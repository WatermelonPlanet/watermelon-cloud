package com.watermelon.user.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.user.server.mapper.entity.SysThirdUserDo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 第三方用户
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Mapper
public interface SysThirdUserMapper extends BaseMapper<SysThirdUserDo> {
}
