# pro-low-complex

## feature

- 支持文件上传
- 支持py文件上传至指定目录
- 支持读取指定目录py文件
- 支持传参数调py脚本
- 支持form表单提交

## Quick Start

- 下载源代码，`git clone git@github.com:AAA-AA/pro-low-complex.git`

- 进入工程目录，`cd  pro-low-complex/`，执行：`mvn clean package -Dmaven.test.skip=true`

- 启动项目，`java -jar target/pro-low-complex-1.0-SNAPSHOT.jar`，启动后，访问：http://localhost:8085/index

## FQ

1. 如何在服务器上部署
- 将jar包上传至服务器，直接以java -jar启动

2. 如果要修改页面在哪修改
- 在templates的index.html页面里修改

3. web服务端入口在哪？
- 服务端入口统一在EntranceController类，后期里面需要补充业务逻辑

4. 用到了哪些技术
- 前端页面展示：vue+element-ui，后端：spring-boot全家桶

