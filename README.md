# watermelon-cloud 


watermelon-cloud 是一个基于springboot3.0 、spring-authorization-server、oauth2.1 等集成的微服务基础框架



## 模块介绍

😄 **watermelon-cloud** 划分为如下几个模块（后续有需要会进行增加）

- `watermelon-cloud`
    - `watermelon-authorization-server` [认证、授权服务]
    - `watermelon-common` [公共模块]
        - `watermelon-common-core` [公共的枚举或常量等]
        - `watermelon-common-mybatis` [mybatis-plus]
    - `watermelon-gateway` [网关服务]
    - `watermelon-user`[用户服务模块]
        - `watermelon-user-api`[用户服务对外提供调用的api]
        - `watermelon-tenant-user-server`[用户服务]



### watermelon-authorization-server

`watermelon-authorization-serve` 认证、授权服务，同时也是一个客户端


### watermelon-user

**watermelon-user-api**

`watermelon-user-api`这个模块就比较有意思了，这个地方设计了自定义了stater，以及在 **FeignClient**上再封装了一层了，这样做的优势在于当我们的FeignClient的接口结构有变化时，通过封装的这一层进行处理，则对外提供的api不会有任何影响。




说明：使用init 分支