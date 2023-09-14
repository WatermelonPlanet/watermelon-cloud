package com.watermelon.common.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 第三方登录类型
 * @author byh
 * @date 2023-08-17 15:10
 * @description
 */
@Getter
@RequiredArgsConstructor
@ToString
public enum AccountPlatform {

    WX,
    QQ,
    GITEE,
    GITHUB;
}
