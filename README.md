# 1.项目maven打包
# 2.需要权限管理的项目pom引用此依赖
```
<dependency>
    <groupId>com.youzidata</groupId>
    <artifactId>authentication-plugin</artifactId>
    <version>1.0.0.RELEASE</version>
    <scope>system</scope>
    <systemPath>${pom.basedir}/lib/authentication-plugin-1.0.0.RELEASE.jar</systemPath>
</dependency>
```
# security.properties 配置文件
配置权限管理数据库，记录用户角色权限关系
