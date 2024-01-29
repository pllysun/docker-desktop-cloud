1. 父pom.xml中写了注释，可以按需求改
2. api模块放所用数据库映射实体类、mapper、响应类等公用部分，会引入其他模块使用(要改名字就模仿创一个，直接新建模块后按需求复制调整pom.xml等，注意删除模块时父pom.xml中模块信息要自己删)
   (需要其他公用模块和这个差不多，就内容不一样且没有启动类，在其他模块的pom.xml中作为依赖引入使用，具体引用方式与导入其他jar包相同，参考user模块的pom.xml的第一个<dependency>)
3. user模块对应各个处理请求的微服务模块(对应api模块中的user类和mapper)，需要启动类、yaml等，必要的service和controller写在这(一般需要用哪些数据库表就写对应的就行，如果想跨模块调用可以学下openfeign，不过在参数包含文件时用openfeign有坑可以百度下，**当然嫌麻烦可以不用远程调用，直接把需要的方法逻辑在当前模块再写一遍**)
   (附带提醒一下，高版本openfeign的引入需要额外配负载均衡，可能麻烦些) 端口设的8080
4. gateway模块，网关，前端直接请求网关后由网关分发请求，这里通过yml配置路由(如有必要可以通过代码添加路由)(不知道可不可以设置多个网关让前端轮询访问，感觉多网关才是比较合理的微服务，不然网关断了就全没法访问了) 端口设的9527
5. 看看todo 有些 提示 或者 不知道是否必须要有的东西

User类对应的数据库表创建SQL(yml中参数记得调整)：
```
CREATE DATABASE `knowledge_communities`; # 数据库
USE `knowledge_communities`
# 用户表
CREATE TABLE users (
  id BIGINT PRIMARY KEY COMMENT '编号',
  username VARCHAR(255) NOT NULL UNIQUE COMMENT '账号',
  pwd VARCHAR(255) NOT NULL COMMENT '密码',
  nick VARCHAR(255) NOT NULL COMMENT '昵称',
  icon VARCHAR(255) NOT NULL COMMENT '头像(地址)',
  biography VARCHAR(255) NOT NULL DEFAULT '' COMMENT '个人简介',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  sys_role INT NOT NULL DEFAULT 0 COMMENT '系统角色(与用户在社群中的角色区分) - 0:系统用户 -1:系统管理员',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记'
) COMMENT '用户表';  
```