# AircraftWar-for-Android

面向对象实践课程项目，在导论课程实验的基础上将飞机大战游戏移植到安卓平台，增加了注册登录以及联机功能。

- 联机以及登陆注册使用socket实现，服务器端代码在`ServerForAircreftWar`下，用idea编写。
- 客户端排行榜以及服务器端用户信息使用sqlite数据库储存。

## 使用到的一些外部库

在`java-lib`文件夹下

- 使用json格式在服务器端和客户端传输数据

  在`json-lib`文件夹下，客户端和服务器端都需要导入。

- 服务器端使用sqlite数据库

  在`sqlite-lib`文件夹下，服务器端需要导入。

