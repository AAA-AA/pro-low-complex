# pro-low-complex

## Feature

- 支持文件上传
- 支持py文件上传至指定目录
- 支持读取指定目录py文件
- 支持传参数调py脚本
- 支持form表单提交

## Quick Start

- 下载源代码，`git clone git@github.com:AAA-AA/pro-low-complex.git`

- 进入工程目录，`cd  pro-low-complex/`，执行：`mvn clean package -Dmaven.test.skip=true`

- 启动项目，`java -jar target/pro-low-complex-1.0-SNAPSHOT.jar`，启动后，访问：http://localhost:8085/index

## py脚本测试数据流向细节
- 前端输入参数
![入口1](http://ww1.sinaimg.cn/large/662ea8e5gy1g6cscpzlbxj22r017wwj6.jpg)
- 前端代码入口处
![前端代码入口1](http://ww1.sinaimg.cn/large/662ea8e5gy1g6csgyc9wrj226o0w8tig.jpg)
![前端代码入口2](http://ww1.sinaimg.cn/large/662ea8e5gy1g6cshjj9uoj21sw0xg45s.jpg)
- 后端接收入口
![后端接收入口](http://ww1.sinaimg.cn/large/662ea8e5gy1g6csqyopesj21qs1jwh19.jpg)
- py执行脚本
![py执行脚本](http://ww1.sinaimg.cn/large/662ea8e5gy1g6csrbtxuqj228g1nsale.jpg)
- 前端接收返回结果
![前端接收返回结果](http://ww1.sinaimg.cn/large/662ea8e5gy1g6cstz5oxbj22581a0ak0.jpg)
- 展示返回结果
![](http://ww1.sinaimg.cn/large/662ea8e5gy1g6cssq2j0uj22ek10w77u.jpg)


## FQ

1. 如何在服务器上部署
- 将jar包上传至服务器，直接以java -jar启动

2. 如果要修改页面在哪修改
- 在templates的index.html页面里修改

3. web服务端入口在哪？
- 服务端入口统一在EntranceController类，后期里面需要补充业务逻辑

4. 用到了哪些技术
- 前端页面展示：vue+element-ui，后端：spring-boot全家桶

