# moment_service
## Pulingle动态服务 
提供用户注册、登录、信息修改、好友系统等相关服务。

###  服务功能
* 1.动态管理：
   * 1.1	动态发布
用户可点击在顶部导航的发布动态按钮进行动态的快捷发布。点击按钮后，弹出发布新动态弹框。弹框有文编编辑区域，以及插入图片按钮。用户在动态文本区域中进行动态的文字输入（限制240字）。点击插入图片按钮，用户选择图片文件后确认进行图片上传（调用图片上传接口）对接口返回的图片URL等图片信息进行记录，用户选择该动态的隐私程度，最后点击发布按钮进行动态的发布。后台对该动态的所有信息进行校验后存入库中。
  * 1.2	动态浏览
每则动态信息显示用户昵称，用户头像，动态发布时间，动态文字内容，动态图片内容，点赞数，评论数。
     *  1.2.1	个人动态浏览
每个用户拥有一个个人主页，个人主页展示该用户所发过的动态记录。若用户访问的是自己主页则显示全部记录,若访问他人的则会显示相对于权限的动态。如为好友则显示隐私程度为好友以上的动态，访客则只显示公开的动态，用户好友拥有该用户好友则会显示二次好友以上的动态。动态的记录数是依照登录用户的权限进行校验。
     *  1.2.2	好友动态查看
用户点击导航栏的动态，跳转到动态页面。其中好友动态则会显示该用户好友的所有动态信息，以最新日期以此排列。
     *  1.2.3	推荐动态查看
用户点击推荐动态页面后，会显示好友的好友，但并非自己好友的用户动态，从中选取一定数量进行展示。
* 2.评论管理：
  * 2.1  评论浏览
用户可以查看每一则动态的评论。用户在对应动态一栏点击评论按钮，评论
信息栏展开，每一则评论显示评论用户昵称，头像，评论时间以及评论内容。若该动态的评论数超过一定数量，则进行分页。评论一栏显示分页标签等，用户点击页面数进行评论消息的浏览。
  * 2.2  评论发布
用户可以针对每一则看到动态进行评论。用户在点击评论按钮后，评论信息栏展开在评论文本区中填入评论内容，确认后点击发表按钮或回车键后发送评论信息。后台对相应数据进行校验，无误后入库记录。错误返回错误信息。后台除了MySQL记录评论数据外，用Redis记录评论Id，一个动态对应一个Set集合，集合记录评论ID.
  *  2.3  用户点赞
用户可以针对每一则看到的动态进行点赞或取消点赞。若用户已对该则动态点赞过了，则点赞按钮（红心）为红色实心，没有则为灰色空心红心。再次点击会取消点赞。后台逻辑实现如下：采用Redis Set集合维护，每一则动态对应一个Set集合，集合存储用户ID，点赞则集合添加该用户ID,取消则移除该ID。
## 关于Pulingle
### Pulingle简介
* Pulingle是我们小组三人（[zkTom](https://github.com/zkTom)、[TeemoSmithLee](https://github.com/TeemoSmithLee)）的本科毕业设计项目,是基于SpringCloud微服务架构的微社交应用。我们应用意在参考微信的朋友圈功能，设计一个功能更为简单的，界面更为简洁，包含动态发布、私信发送、图片分享等功能的社交软件。</br>
### 主要功能
1.	用户通过手机号，在进行短信验证码之后进行注册。
2.	用户注册之后可以凭注册账号密码进行登录，以进行应用提供功能服务。
3.	用户可以通过简单的操作进行文章，图片动态发布。
4.	用户可以浏览自己过往发布的动态消息，以及其评论消息。
5.	用户可以浏览朋友的动态，功能类比微信的朋友圈。并且可对其动态进行评论。
6.	用户可以上传照片到相册，并可以浏览。
7.	用户好友之间可以进行消息发送，并对用户新消息的推送。
8.	用户对账号资料的修改保存
###  项目架构

对功能的分析，以及结合微服务架构设计，把整个项目大致划分为4个服务。服务之间通过服务治理实现相互调用。
* 用户服务：提供用户注册、登录、信息修改、好友系统等相关服务。
* 动态服务：用户发布动态、浏览他人如好友或二次好友动态、评论他人动态等服务。
* 图片服务：提供用户上传图片、管理图片、相册图片管理等相对应服务。
* 消息服务：用户之间私信收发，系统消息发送，验证码，通知等服务。
![功能图](https://pulingle.oss-cn-shenzhen.aliyuncs.com/%E5%8A%9F%E8%83%BD%E5%9B%BE.png)

* 架构设计 
![Pulingle架构图](https://pulingle.oss-cn-shenzhen.aliyuncs.com/Pulingle%E6%9E%B6%E6%9E%84%E5%9B%BE%20%282%29.png)

组件说明：
Spring Cloud 组件：
* Eureka Server: 提供在分布式环境下的服务发现，服务注册的功能。每个服务及组件服务都在其中注册以及发现。
* 网关服务Zuul:边缘服务工具，是提供动态路由，监控，弹性，安全等的边缘服务。
* Config Server: 配置管理开发工具包，可以让你把配置放到远程服务器，目前支持本地存储、Git以及Subversion。这里我们采用从远程Github拉取配置信息。远程配置配置文件加密存储，从ConfigServer拉取时经过解密。
* Spring Cloud Bus: 事件、消息总线，用于在集群（例如，配置变化事件）中传播状态变化
* Git Repo:在Github上提供配置信息。
* RabbitMQ: 用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面都非常的优秀。是当前最主流的消息中间件之一。
#### 技术栈
* 后端</br>
•	Maven 3</br>
•	Java 8</br>
•	SpringCloud（Eureka、Zuul、Ribbon、Feign、Config）</br>
•	SpringBoot+MyBatis</br>
•	MySQL 5.7.22</br>
•	Redis 3.0.6</br>
* 前端</br>
•	node >= 8.9.3</br>
•	npm >= 5.5.1</br>
•	vue  </br>
•	axios  </br>
•	js-cookie  </br>
•	lodash </br>
•	vuex</br>
•	es6-promise</br>
•	photoswipe</br>
•	vue-lazyload </br>
* 使用RAP2进行接口文档管理: [RAP2](https://github.com/thx/RAP)
* 使用阿里云OSS服务作图片资源空间
* 短信验证码使用阿里云短信服务
#### 项目代码链接
* 微服务</br>
用户服务:   [user-service](https://github.com/Konoha-orz/user_service)</br>
消息服务:   [message-service](https://github.com/Konoha-orz/message_service)</br>
图片服务:   [picture-service](https://github.com/Konoha-orz/picture_service)</br>
动态服务:   [moment-service](https://github.com/Konoha-orz/moment_service)</br>
* Spring Cloud 组件</br>
服务发现：[eureka_server](https://github.com/Konoha-orz/eureka_server)</br>
服务网关：[gateway_zuul](https://github.com/Konoha-orz/gateway_zuul)</br>
服务配置：[config_server](https://github.com/Konoha-orz/config_server)</br>
* 统一管理配置</br>
[Pulingle-Config-Repo](https://github.com/Konoha-orz/Pulingle-Config-Repo)</br>
#### 项目部署
我们当初的项目是部署在阿里云ECS学生服务器上的。确保已安装环境依赖Java/MySQL/Redis</br>
* 部署运行顺序：</br>
1.eureka_server</br>
2.config_server和gateway_zuul</br>
3.user-service、message-service、picture-service、moment-service</br>
（注：确保根据jar运行环境在同一管理配置[Pulingle-Config-Repo](https://github.com/Konoha-orz/Pulingle-Config-Repo)配置好对应的信息，如端口、IP等）
###  项目演示</br>
* [视频演示地址](https://pulingle.oss-cn-shenzhen.aliyuncs.com/Pulingle%E6%BC%94%E7%A4%BA%E5%BD%95%E5%B1%8F.mp4)</br>
