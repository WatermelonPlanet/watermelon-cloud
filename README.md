# watermelon-cloud 


watermelon-cloud
>Spring Boot 3.1 \
>Spring Authorization Server 1.1.1\
>Spring-cloud 2022.0.3\
>Spring-cloud-alibaba 2022.0.0.0\
>Spring-cloud-gateway



## 模块介绍

**watermelon-cloud** 划分为如下几个模块（后续有需要会进行增加）

- `watermelon-cloud`
    - `watermelon-authorization` [认证、授权模块]
      - `watermelon-authorization-server`[认证、授权服务模块] 
      - `watermelon-authorization-oauth2-client`[oauth2-client封装的扩展接口] 
      - `watermelon-authorization-user-core`[用户、客户端查询相关code] 
    - `watermelon-common` [公共模块]
        - `watermelon-common-core` [公共的枚举或常量等]
        - `watermelon-common-mybatis` [mybatis-plus]
        - `watermelon-common-redis` [redis服务]
    - `watermelon-gateway` [网关服务]
    - `watermelon-tenant`[租户模块]
        - `watermelon-tenant-user-api`[租户用户服务对外提供调用的api]
        - `watermelon-tenant-user-server`[租户用户服务]
      
    - `...其他模块待定`



[🍉Spring Authorization Server原理](https://juejin.cn/post/7279629380749033491)