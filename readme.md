[TOC]

# ShardingSphere测试工程

> 本测试demo基于*spring boot 2.7.8-SNAPSHOT*和*shardingsphere-jdbc-core-spring-boot-starter 5.2.1*开发

在编写demo时发现，shardingSpere与spring boot和spring data jpa的版本兼容性很重要，但是网上找不到公开的兼容性信息，所以最好参考github上的示例项目

[示例项目1 - spring data jpa集成sharding-jdbc](https://github.com/realpdai/tech-pdai-spring-demos/blob/main/243-springboot-demo-shardingjdbc-jpa-tables/pom.xml)

<mark>注意：sharding-sphere处于高速开发阶段，所有除官网文档之外的参考资料很容易过时，且sharding-sphere配置项随时可能改变，一切以官网文档为准！</mark>

## sharding-jdbc测试

首先需要引入maven依赖，并进行初始化配置

**注意：需要手动创建分表，使用spring data jpa只能自动创建一张表**

```xml
<!--  Sharding-JDBC  -->
<dependency>
   <groupId>org.apache.shardingsphere</groupId>
   <artifactId>shardingsphere-jdbc-core-spring-boot-starter</artifactId>
   <version>5.2.1</version>
</dependency>
<!-- 低于1.33版本的snakeyaml会导致shardingsphere启动失败 -->
<dependency>
   <groupId>org.yaml</groupId>
   <artifactId>snakeyaml</artifactId>
   <version>1.33</version>
</dependency>
```

初始化配置分为两部分，一部分是用来令jpa自动创建表的spring datasource配置，另一部分是用来使sharding-jdbc运行的配置

1. spring datasource配置

    ```yaml
    spring:
      datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/sharding_db1
      username: root
      password: password
    ```
 
2. sharding-jdbc配置

    在配置时，应以官网文档为准，因为sharding-jdbc已经迭代了很多版本，从博客或其他网站看到的信息并不能保证实效性

    ```yaml
    # 配置真实数据源
    spring.shardingsphere.datasource.names=ds0,ds1
    
    # 配置第 1 个数据源
    spring.shardingsphere.datasource.ds0.type=com.zaxxer.hikari.HikariDataSource
    spring.shardingsphere.datasource.ds0.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.shardingsphere.datasource.ds0.jdbc-url=jdbc:mysql://localhost:3306/sharding_db1
    spring.shardingsphere.datasource.ds0.username=root
    spring.shardingsphere.datasource.ds0.password=password
    
    # 配置第 2 个数据源
    spring.shardingsphere.datasource.ds1.type=com.zaxxer.hikari.HikariDataSource
    spring.shardingsphere.datasource.ds1.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.shardingsphere.datasource.ds1.jdbc-url=jdbc:mysql://localhost:3306/sharding_db2
    spring.shardingsphere.datasource.ds1.username=root
    spring.shardingsphere.datasource.ds1.password=password
    ```
   
3. 启动mysql docker容器

   ```shell
   docker run --name sharding-mysql -p 3306:3306 -p 33060:33060 \
   -e MYSQL_ROOT_PASSWORD=password \
   -e TZ=Asia/Shanghai \
   -d mysql:8.0.31
   ```
   
---
## 遇到的问题

### sharding-sphere与spring boot版本兼容问题

这其实是一个伪问题，sharding-sphere与spring boot并不存在依赖问题。当在spring boot环境下使用sharding-jdbc时，只需要直接引入对应的starter即可

```xml
<!--  Sharding-JDBC集成spring boot  -->
<dependency>
   <groupId>org.apache.shardingsphere</groupId>
   <artifactId>shardingsphere-jdbc-core-spring-boot-starter</artifactId>
   <version>5.2.1</version>
</dependency>
<!-- 不要使用下面这个！这是已经过期的版本，其配置项与官网最新文档并不相同 -->
<dependency>
   <groupId>org.apache.shardingsphere</groupId>
   <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
   <version>4.1.1</version>
</dependency>
```

如果要单独使用sharding-jdbc（不使用spring boot），考虑如下两个依赖

```xml
<!-- 这两个依赖并不相同，如果要单独使用sharding-jdbc，需要查阅相关资料，了解这两个包的作用 -->
<dependency>
   <groupId>org.apache.shardingsphere</groupId>
   <artifactId>shardingsphere-jdbc-core</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.shardingsphere</groupId>
   <artifactId>sharding-jdbc-core</artifactId>
</dependency>
```

### sharding-jdbc分库配置没有正确工作

进行了sharding-jdbc分库配置后，发现并没有生效，数据随机分布到了不同库的表中。经排查，是因为使用了错误的依赖，导致很多properties配置文件中的配置没有生效

本测试demo使用的sharding-jdbc依赖为`shardingsphere-jdbc-core-spring-boot-starter`，不是任何其他的依赖！

> sharding-jdbc在版本更新时，依赖包在maven central的coordination发生了改变，且不同大版本的配置内容极可能不同，生产环境需要慎重选择版本

<mark>网上的很多资料都是根据老版本的sharding-sphere或sharding-jdbc，已经失去时效性！具体依赖包与配置手册以官方文档为准！</mark>

如果只需要使用分库分表功能，只需引入下面的依赖

```xml
<!--  Sharding-JDBC集成spring boot  -->
<dependency>
   <groupId>org.apache.shardingsphere</groupId>
   <artifactId>shardingsphere-jdbc-core-spring-boot-starter</artifactId>
   <version>5.2.1</version>
</dependency>
<!-- 低于1.33版本的snakeyaml会导致shardingsphere启动失败 -->
<dependency>
   <groupId>org.yaml</groupId>
   <artifactId>snakeyaml</artifactId>
   <version>1.33</version>
</dependency>
```

```properties
# 示例配置 基于shardingsphere-jdbc-core-spring-boot-starter 5.2.1版本
# suppress inspection "SpringBootApplicationProperties" for whole file
# 因为将idea集成spring boot的插件不能正确识别sharding-jdbc的配置，写在yaml中会报错，
# 很碍眼，因此单独写在.properties中

# 配置真实数据源
spring.shardingsphere.datasource.names=ds0,ds1

# 配置第 1 个数据源
spring.shardingsphere.datasource.ds0.type=com.zaxxer.hikari.HikariDataSource
spring.shardingsphere.datasource.ds0.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.ds0.jdbc-url=jdbc:mysql://localhost:3306/sharding_db1
spring.shardingsphere.datasource.ds0.username=root
spring.shardingsphere.datasource.ds0.password=password

# 配置第 2 个数据源
spring.shardingsphere.datasource.ds1.type=com.zaxxer.hikari.HikariDataSource
spring.shardingsphere.datasource.ds1.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.ds1.jdbc-url=jdbc:mysql://localhost:3306/sharding_db2
spring.shardingsphere.datasource.ds1.username=root
spring.shardingsphere.datasource.ds1.password=password

# 配置分库分表策略，其语法遵循groovy dsl
spring.shardingsphere.rules.sharding.tables.cm_user.actual-data-nodes=ds$->{0..1}.cm_user
spring.shardingsphere.rules.sharding.tables.user_order.actual-data-nodes=ds$->{0..1}.user_order$->{1..3}
# 配置分库策略
spring.shardingsphere.rules.sharding.tables.cm_user.database-strategy.standard.sharding-column=id
spring.shardingsphere.rules.sharding.tables.cm_user.database-strategy.standard.sharding-algorithm-name=database_inline_user
spring.shardingsphere.rules.sharding.tables.user_order.database-strategy.standard.sharding-column=user_id
spring.shardingsphere.rules.sharding.tables.user_order.database-strategy.standard.sharding-algorithm-name=database_inline_user_order
# 配置分表策略
spring.shardingsphere.rules.sharding.tables.user_order.table-strategy.standard.sharding-column=user_id
spring.shardingsphere.rules.sharding.tables.user_order.table-strategy.standard.sharding-algorithm-name=table_inline_user_order
# 配置分片算法
spring.shardingsphere.rules.sharding.sharding-algorithms.database_inline_user.type=INLINE
spring.shardingsphere.rules.sharding.sharding-algorithms.database_inline_user.props.algorithm-expression=ds${id % 2}
spring.shardingsphere.rules.sharding.sharding-algorithms.database_inline_user_order.type=INLINE
spring.shardingsphere.rules.sharding.sharding-algorithms.database_inline_user_order.props.algorithm-expression=ds${user_id % 2}
spring.shardingsphere.rules.sharding.sharding-algorithms.table_inline_user_order.type=INLINE
spring.shardingsphere.rules.sharding.sharding-algorithms.table_inline_user_order.props.algorithm-expression=user_order${order_id % 3}

```

### sharding-jdbc无法识别结尾数字为0的表

当设置分表时，如果设置三张分表：`user_order0`, `user_order1`, `user_order2`，会发现sharding-jdbc不能正确识别三张表，反而会尝试向后缀为1、2、3的表里插入数据

经排查，发现是配置文件写错了。**actual-data-nodes和表达式中使用的数据节点必须严格匹配**

错误的配置：

```properties
# actual-data-nodes和表达式不匹配
spring.shardingsphere.rules.sharding.tables.user_order.actual-data-nodes=ds$->{0..1}.user_order$->{1..3}
...
spring.shardingsphere.rules.sharding.sharding-algorithms.table_inline_user_order.props.algorithm-expression=user_order${id % 3}
```

正确的配置：

```properties
# actual-data-nodes和表达式匹配
spring.shardingsphere.rules.sharding.tables.user_order.actual-data-nodes=ds$->{0..1}.user_order$->{0..2}
...
spring.shardingsphere.rules.sharding.sharding-algorithms.table_inline_user_order.props.algorithm-expression=user_order${id % 3}
```

---

## 参考链接

1. [sharding-sphere jdbc官方文档 - 5.0.0](https://shardingsphere.apache.org/document/5.0.0/cn/user-manual/shardingsphere-jdbc/usage/sharding/spring-boot-starter/#%E5%9C%A8-spring-%E4%B8%AD%E4%BD%BF%E7%94%A8-shardingspheredatasource)