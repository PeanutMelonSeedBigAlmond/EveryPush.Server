# 使用方法

## 先决条件

1. 运行时：Java 17+
2. 外部依赖：MySQL 8
3. 要求能访问 firebase 服务
4. 已在 [firebase 控制台](https://console.firebase.google.com/) 中添加项目

### 开始使用

1. 从 `firebase 控制台/项目/项目设置/服务账号` 中，生成私钥并下载
2. 下载编译好的二进制文件
3. 将第 2 步中的二进制文件与第 1 步中的私钥文件放入同一文件夹，并将私钥文件重命名为 `firebase-adminsdk.json`
4. 运行，若需修改运行时设置，参考 java 和 springboot 运行选项
   1. 默认数据库连接为 `jdbc:mysql://localhost:3306/push_server`，可以通过参数 `--spring.datasource.url=<URL>` 修改
   2. 默认数据库用户名和密码为空，可以通过参数 `--spring.datasource.username=<username>`
      和 `--spring.datasource.username=<password>` 修改
5. 默认监听端口: `8091`，默认监听地址：`0.0.0.0`

### Docker 地址

    `ghcr.io/peanutmelonseedbigalmond/pushserver`

`ci-latest` 为最新 ci 构建版本

二进制文件位于 `/app/app.jar`

### 编译

```bash
./gradlew :bootJar
```