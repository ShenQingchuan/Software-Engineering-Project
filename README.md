## 组员分工：

- 「**组长**」何飘
- 「**Axure UI 设计**」林浩仪
- 「**前端**」唐梦予
- 「**后端**」秦先富 & 宋博宇

# 社区综合治理系统 - 前端

> 本项目基于Vue.js 2.x + Vue-Router + Vuex + SCSS + Element-UI构建
> 
> 四川师范大学 2017 级 4 班软件工程课程大作业「Web 系统开发」
> 
> ​											成员：唐梦予、林浩怡

# 社区综合治理系统 - 后端

> 本项项目基于SpringBoot、Shiro、Mybatis、Log4j、Docker、Redis进行构建
>
> 四川师范大学 2017 级 4 班软件工程课程大作业「Web 系统开发」
>
> ​											成员：何飘、秦先富、宋博宇

1. ### SpringBoot

   * Mvc设计模式：controller、service、serviceImpl（有更好的代码规范）

   * 注解：@Autowired、@Resource相同点和不同点（Java bean三种注入方式）

   * application.properties和application.yml配置文件使用体验

   * 不再是傻乎乎每次新建项目都在pom.xml文件里添加阿里云镜像

   * 学会从External Libraries中了解所加入的项目依赖（管理对冲突的依赖）

   * URL中PathVariable类型（数字、字母、符号）判断对数据库安全操作十分重要

   * 使用jasypt对数据库password加密，在测试类中测试确保加密后的密码没有问题

   * 更加深化了我对Java面向对象的理解

     

2. ## Shiro

   * 认识authc、anon、perms、role授权管理方式
   * 了解认证、授权代码执行流程
   * 后台获取Cookie或Authorization中JSESSIONID
   * 不同域名下后台实现跨域配置
   * 了解了session会话管理

   

3. ### Mybatis

   * Jpa过渡到Mybatis，为什么要使用Mybatis？

   * 使用Mybatis注解+Sql语句实现数据库操作

   * 在resource下新建Mapper.xml,为什么我们最终选择这种方式？

   * 实现Mybatis的动态Sql所运用的标签

   * returnType与returnMap你真的了解多少？

   * parameterType是不是每次有参数传入都必须用？

   * 你怎样传多个数量可变的参数，并且使用一条Sql将所有可变参数修改或插入

   * Mybatis中简单的二级缓存设置（*.xml和application.yml）

   * Mybatis配合Database插件，快速修改数据库数据、编写Sql

   * 在因外键而产生关联的几张表中，你的实体类改如何建？returnMap中成员类的子标签应该使用什么

     

   * ```xml
     <mapper namespace="com.example.csgs.mapper.UserMapper"> //它代表着什么
     ```

   

4. ### Log4j

   * ```java
     //使用Lombok注解@Log4j完全可以省略下方代码
     private static Logger logger = Logger.getLogger(Test.class);
     ```

   * log4j.properties创建，规定日志输出地点及类型

   * 在application.yml中将Mybatis的log-impl替换成Log4j

   * 在你需要打印INFO的地方，使用@Log4j注解，快速实现日志输出

     

5. ### Docker

   * 从手动上传jar包构建镜像到docker-plugin一键搞定，这期间我到底经历了什么

   * 将容器中的log映射到Host：-v /web_log:/log

   * 在Dockerfile中配置容器时区配置

     ```dockerfile
     FROM java:8
     
     VOLUME /tmp
     
     COPY csgs-0.0.1-SNAPSHOT.jar csgs.jar
     
     ENV TZ=Asia/Shanghai
     
     RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone
     
     ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/csgs.jar"]
     ```

6. ### Redis

   * 用Docker直接拉一个Redis的images，run起来，it's over！！！
   * 用来存sessionId和token，这些访问频繁、单条信息量不大的内容


