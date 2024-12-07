### 基于SpringBoot + Vue小程序商城

#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios
报表：Spread.js

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok

管理员数据统计，管理员设计师审核，商品管理，设计师管理，订单管理，订单评价，公告信息管理，论坛管理，论坛回复，用户与设计师消息通知，用户收货地址管理

用户申请设计师，用户订单添加购物车，订单付款评价，论坛消息回复，收货地址新增修改，修改商品信息

#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 后端启动方式

1.首先启动redis，进入redis目录终端。输入redis-server回车
2.导入sql文件，修改数据库与redis连接配置
3.idea中启动后端项目

#### 示例代码

``` java
// 用户登陆注册
String openid = JSON.parseObject(res).get("openid").toString();
System.out.println("openid" + openid);
Integer count = userInfoService.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid));
if (count > 0) {
    return R.ok(userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid)));
} else {
    user.setOpenId(openid);
    user.setCreateDate(DateUtil.formatDateTime(new Date()));
    user.setCode("U-"+new Date().getTime());
    user.setType(1);
    userInfoService.save(user);
    return R.ok(user);
}
```

``` java
// 查找聊天信息
if (takeUser == userId) {
    messageInfoService.update(Wrappers.<MessageInfo>lambdaUpdate().set(MessageInfo::getTaskStatus, 1)
            .eq(MessageInfo::getTakeUser, takeUser).eq(MessageInfo::getSendUser, sendUser));
} else {
    messageInfoService.update(Wrappers.<MessageInfo>lambdaUpdate().set(MessageInfo::getTaskStatus, 1)
            .eq(MessageInfo::getTakeUser, sendUser).eq(MessageInfo::getSendUser, takeUser));
}
return R.ok(messageInfoService.getMessageDetail(takeUser, sendUser));
```

``` sql
-- 订单统计
SELECT
DATE_FORMAT( spo.days, '%m-%d' ) AS days,
COUNT( oi.`code` ) AS count,
IFNULL( SUM( oi.price ), 0 ) AS orderPrice
FROM
(
SELECT
    DATE_SUB( curdate(), INTERVAL + 0 DAY ) days UNION
SELECT
    DATE_SUB( curdate(), INTERVAL + 1 DAY ) UNION
SELECT
    DATE_SUB( curdate(), INTERVAL + 2 DAY ) UNION
SELECT
    DATE_SUB( curdate(), INTERVAL + 3 DAY ) UNION
SELECT
    DATE_SUB( curdate(), INTERVAL + 4 DAY ) UNION
SELECT
    DATE_SUB( curdate(), INTERVAL + 5 DAY ) UNION
SELECT
DATE_SUB( curdate(), INTERVAL + 6 DAY )) spo
LEFT JOIN order_info oi ON (
DATE_FORMAT( oi.create_date, '%Y-%m-%d' ) = DATE_FORMAT( spo.days, '%Y-%m-%d' ))
GROUP BY
days
ORDER BY days ASC
```

``` sql
-- 根据商铺获取订单统计
SELECT
COUNT( 1 ) AS orderNum,
IFNULL( SUM( oi.price ), 0 ) AS orderPrice,
ui.avatar,
ui.user_name,
si.tag,
si.introduction
FROM
order_info oi
LEFT JOIN commodity_info ci ON ( ci.id = oi.commodity_id )
LEFT JOIN shop_info si ON ( si.id = ci.shop_id )
LEFT JOIN user_info ui ON ( ui.id = si.user_id )
WHERE
1 = 1
GROUP BY
si.id
```


``` sql
-- 本月收益占比
SELECT
COUNT( oi.commodity_id ) AS orderNum,
IFNULL( SUM( oi.price ), 0 ) AS orderPrice,
ci.`name`
FROM
order_info oi
LEFT JOIN commodity_info ci ON ( ci.id = oi.commodity_id )
WHERE
1 = 1
AND DATE_FORMAT( oi.create_date, '%Y-%m' ) = DATE_FORMAT( NOW(), '%Y-%m' )
GROUP BY
oi.commodity_id
```

#### 默认后台账户密码
[管理员]
admin
1234qwer

#### 项目截图

暂无


#### 演示视频

[项目视频：基于SpringBoot小程序商城](https://www.bilibili.com/video/BV1dP4y1N7wp/)

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.黑奴价格

> 项目部署调试不好包退！功能逻辑没讲明白包退！

#### 其它资源

[2024年答辩顺利通过](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年答辩顺利通过](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年答辩通过率100%](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)


#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)

