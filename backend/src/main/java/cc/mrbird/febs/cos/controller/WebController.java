package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebController {

    private final IUserInfoService userInfoService;

    private final IAuditInfoService auditInfoService;

    private final IAddressInfoService addressInfoService;

    private final IBulletinInfoService bulletinInfoService;

    private final IShopInfoService shopInfoService;

    private final IPostInfoService postInfoService;

    private final ICommodityInfoService commodityInfoService;

    private final IReplyInfoService replyInfoService;

    private final IMessageInfoService messageInfoService;

    private final IOrderInfoService orderInfoService;

    private final IEvaluationService evaluationService;

    @PostMapping("/userAdd")
    public R userAdd(@RequestBody UserInfo user) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wx76a6577665633a86";//自己的appid
        url += "&secret=78070ccedf3f17b272b84bdeeca28a2e";//自己的appSecret
        url += "&js_code=" + user.getOpenId();
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);    //GET方式
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()          // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)                    // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)             // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)                    // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(false).build();           // 将上面的配置信息 运用到这个Get请求里
        httpget.setConfig(requestConfig);                         // 由客户端执行(发送)Get请求
        response = httpClient.execute(httpget);                   // 从响应模型中获取响应实体
        HttpEntity responseEntity = response.getEntity();
        System.out.println("响应状态为:" + response.getStatusLine());
        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            System.out.println("响应内容为:" + res);
        }
        // 释放资源
        if (httpClient != null) {
            httpClient.close();
        }
        if (response != null) {
            response.close();
        }
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
    }

    @RequestMapping("/openid")
    public R getUserInfo(@RequestParam(name = "code") String code) throws Exception {
        System.out.println("code" + code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wx9fffb151ded22005";//自己的appid
        url += "&secret=9666e94c91361e7de4d3a6d09a23402f";//自己的appSecret
        url += "&js_code=" + code;
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);    //GET方式
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()          // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)                    // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)             // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)                    // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(false).build();           // 将上面的配置信息 运用到这个Get请求里
        httpget.setConfig(requestConfig);                         // 由客户端执行(发送)Get请求
        response = httpClient.execute(httpget);                   // 从响应模型中获取响应实体
        HttpEntity responseEntity = response.getEntity();
        System.out.println("响应状态为:" + response.getStatusLine());
        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            System.out.println("响应内容为:" + res);
        }
        // 释放资源
        if (httpClient != null) {
            httpClient.close();
        }
        if (response != null) {
            response.close();
        }
        String openid = JSON.parseObject(res).get("openid").toString();
        System.out.println("openid" + openid);
        return R.ok(openid);
    }

    @GetMapping("/subscription")
    public R subscription(@RequestParam("taskCode") String taskCode) throws Exception {
        LinkedHashMap<String, Object> tokenParam = new LinkedHashMap<String, Object>() {
            {
                put("grant_type", "client_credential");
                put("appid", "wx76a6577665633a86");
                put("secret", "78070ccedf3f17b272b84bdeeca28a2e");
            }
        };
        String tokenResult = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", tokenParam);
        String token =  JSON.parseObject(tokenResult).get("access_token").toString();
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>(){
            {
                put("thing1", new HashMap<String, Object>(){
                    {
                        put("value", "张三");
                    }
                });
                put("character_string3", new HashMap<String, Object>(){
                    {
                        put("value", taskCode);
                    }
                });
                put("time4", new HashMap<String, Object>(){
                    {
                        put("value", DateUtil.formatDateTime(new Date()));
                    }
                });
                put("thing5", new HashMap<String, Object>(){
                    {
                        put("value", "若查看详细内容，请登录小程序");
                    }
                });
            }
        };
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + token;
        LinkedHashMap<String, Object> subscription = new LinkedHashMap<String, Object>() {
            {
                put("touser", "oeDfR5zqxQD3EEA3uPT836qnauSc");
                put("template_id", "Z27pBK1n9WnKNtQ_fo7TC-nUJUlOQ9KVJk6LIgp0nH8");
                put("data", data);
            }
        };
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(subscription));
        return R.ok(result);
    }

    /**
     * 进入小程序主页信息
     * @return
     */
    @GetMapping("/home")
    public R home() {
        LinkedHashMap<String, Object> result= new LinkedHashMap<>();
        result.put("commodityHot", commodityInfoService.getCommodityHot());
        result.put("shopInfo", shopInfoService.shopInfoHot());
        result.put("postInfo", postInfoService.getPostListHot());
        return R.ok(result);
    }

    /**
     * 用户添加审核信息
     * @param auditInfo
     * @return
     */
    @PostMapping("/userAuditAdd")
    public R userAuditAdd(@RequestBody AuditInfo auditInfo) {
        auditInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        auditInfo.setAuditStatus(0);
        return R.ok(auditInfoService.save(auditInfo));
    }

    /**
     * 根据用户ID获取审核信息
     * @param userId
     * @return
     */
    @GetMapping("/auditInfoByUser")
    public R auditInfoByUser(@RequestParam Integer userId) {
        return R.ok(auditInfoService.getOne(Wrappers.<AuditInfo>lambdaQuery().eq(AuditInfo::getUserId, userId)));
    }

    /**
     * 根据用户ID获取地址信息
     * @param userId
     * @return
     */
    @GetMapping("/addressInfoByUser")
    public R addressInfoByUser(@RequestParam Integer userId) {
        return R.ok(addressInfoService.list(Wrappers.<AddressInfo>lambdaQuery().eq(AddressInfo::getUserId, userId)));
    }

    /**
     * 根据ID获取地址信息
     * @param addressId
     * @return
     */
    @GetMapping("/addressInfoById")
    public R addressInfoById(@RequestParam Integer addressId) {
        return R.ok(addressInfoService.getById(addressId));
    }

    /**
     * 获取用户默认地址
     * @return
     */
    @GetMapping("/selDefaultAddress")
    public R selDefaultAddress(@RequestParam Integer userId) {
        return R.ok(addressInfoService.getOne(Wrappers.<AddressInfo>lambdaQuery().eq(AddressInfo::getUserId, userId).eq(AddressInfo::getDefaultAddress, 1)));
    }

    /**
     * 用户添加收货地址
     * @param addressInfo
     * @return
     */
    @PostMapping("/addressAdd")
    public R addressAdd(@RequestBody AddressInfo addressInfo) {
        if (addressInfo.getDefaultAddress() == 1) {
            addressInfoService.update(Wrappers.<AddressInfo>lambdaUpdate().set(AddressInfo::getDefaultAddress, 0).eq(AddressInfo::getUserId, addressInfo.getUserId()));
        }
        addressInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(addressInfoService.save(addressInfo));
    }

    /**
     * 用户编辑收货地址
     * @param addressInfo
     * @return
     */
    @PostMapping("/addressEdit")
    public R addressEdit(@RequestBody AddressInfo addressInfo) {
        if (addressInfo.getDefaultAddress() == 1) {
            addressInfoService.update(Wrappers.<AddressInfo>lambdaUpdate().set(AddressInfo::getDefaultAddress, 0).eq(AddressInfo::getUserId, addressInfo.getUserId()));
        }
        return R.ok(addressInfoService.updateById(addressInfo));
    }

    /**
     * 用户删除收获地址
     * @param addressId
     * @return
     */
    @GetMapping("/address/delete")
    public R addressRemove(@RequestParam Integer addressId) {
        return R.ok(addressInfoService.removeById(addressId));
    }

    /**
     * 获取贴子信息
     * @return
     */
    @GetMapping("/getPostList")
    public R getPostList() {
        return R.ok(postInfoService.getPostList());
    }

    /**
     * 根据贴子编号获取详细信息
     * @param postId
     * @return
     */
    @GetMapping("/getPostInfoById")
    public R getPostInfoById(@RequestParam Integer postId) {
        return R.ok(postInfoService.getPostInfoById(postId));
    }

    /**
     * 贴子回复
     * @return
     */
    @PostMapping("/replyPost")
    public R replyPost(@RequestBody ReplyInfo replyInfo) {
        replyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(replyInfoService.save(replyInfo));
    }

    /**
     * 添加贴子
     * @param postInfo
     * @return
     */
    @PostMapping("/postAdd")
    public R postAdd(@RequestBody PostInfo postInfo) {
        postInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(postInfoService.save(postInfo));
    }

    /**
     * 获取公告信息
     * @return
     */
    @GetMapping("/getBulletinList")
    public R getBulletinList() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 查询消息信息
     * @param userId
     * @return
     */
    @GetMapping("/messageListById")
    public R messageListById(@RequestParam Integer userId) {
        return R.ok(messageInfoService.messageListById(userId));
    }

    /**
     * 查找聊天记录
     * @param takeUser
     * @param sendUser
     * @return
     */
    @GetMapping("/getMessageDetail")
    public R getMessageDetail(@RequestParam(value = "takeUser") Integer takeUser, @RequestParam(value = "sendUser") Integer sendUser, @RequestParam(value = "userId") Integer userId) {
        if (takeUser == userId) {
            messageInfoService.update(Wrappers.<MessageInfo>lambdaUpdate().set(MessageInfo::getTaskStatus, 1)
                    .eq(MessageInfo::getTakeUser, takeUser).eq(MessageInfo::getSendUser, sendUser));
        } else {
            messageInfoService.update(Wrappers.<MessageInfo>lambdaUpdate().set(MessageInfo::getTaskStatus, 1)
                    .eq(MessageInfo::getTakeUser, sendUser).eq(MessageInfo::getSendUser, takeUser));
        }
        return R.ok(messageInfoService.getMessageDetail(takeUser, sendUser));
    }

    /**
     * 消息回复
     * @param messageInfo
     * @return
     */
    @PostMapping("/messageReply")
    public R messageReply(@RequestBody MessageInfo messageInfo) {
        messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        messageInfo.setTaskStatus(0);
        return R.ok(messageInfoService.save(messageInfo));
    }

    /**
     * 商品详情
     * @param commodityId
     * @return
     */
    @GetMapping("/goodsDetail")
    public R goodsDetail(@RequestParam Integer commodityId) {
        return R.ok(commodityInfoService.goodsDetail(commodityId));
    }

    /**
     * 查询商品是否在购物车中
     * @param userId
     * @param commodityId
     * @return
     */
    @GetMapping("/selUserCartByGoodsId")
    public R selUserCartByGoodsId(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "commodityId") Integer commodityId) {
        return R.ok(orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserId, userId).eq(OrderInfo::getCommodityId, commodityId).eq(OrderInfo::getOrderStatus, 1)));
    }

    /**
     * 添加到购物车
     * @param orderInfo
     * @return
     */
    @PostMapping("/addGoodsCart")
    public R addGoodsCart(@RequestBody OrderInfo orderInfo) {
        orderInfo.setCode("ORD-"+new Date().getTime());
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setOrderStatus(1);
        // 查询用户默认地址
        AddressInfo addressInfo = addressInfoService.getOne(Wrappers.<AddressInfo>lambdaQuery().eq(AddressInfo::getUserId, orderInfo.getUserId())
                .eq(AddressInfo::getDefaultAddress, 1));
        if (addressInfo != null) {
            orderInfo.setAddressId(addressInfo.getId());
        }
        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 查询用户购物车
     * @param userId
     * @return
     */
    @GetMapping("/selGoodsCart")
    public R selGoodsCart(@RequestParam Integer userId) {
        return R.ok(orderInfoService.selGoodsCart(userId));
    }

    /**
     * 根据订单ID获取订单信息
     * @return
     */
    @GetMapping("/selOrderListByOrderIds")
    public R selOrderListByOrderIds(@RequestParam List<String> ids) {
        return R.ok(orderInfoService.selOrderListByOrderIds(ids));
    }

    /**
     * 购物车完成付款
     * @param ids
     * @return
     */
    @GetMapping("/goodsCartComplete")
    public R goodsCartComplete(@RequestParam List<String> ids) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getOrderStatus, 2).in(OrderInfo::getId, ids)));
    }

    /**
     * 购买商品
     * @param orderInfo
     * @return
     */
    @PostMapping("/buyGoods")
    public R buyGoods(@RequestBody OrderInfo orderInfo) {
        orderInfo.setCode("ORD-"+new Date().getTime());
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setOrderStatus(2);
        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 查询店铺及商品信息
     * @return
     */
    @GetMapping("/selShopDetailList")
    public R selShopDetailList() {
        return R.ok(commodityInfoService.selShopDetailList());
    }

    /**
     * 获取商铺及商品详细信息
     * @param shopId
     * @return
     */
    @GetMapping("/getShopDetail")
    public R getShopDetail(@RequestParam Integer shopId) {
        return R.ok(commodityInfoService.getShopDetail(shopId));
    }

    /**
     * 店铺商品排序方式
     * @param shopId
     * @return
     */
    @GetMapping("/shopCommoditSort")
    public R shopCommoditySort(@RequestParam(value = "shopId") Integer shopId, @RequestParam(value = "type") Integer type) {
        return R.ok(commodityInfoService.shopCommoditySort(shopId, type));
    }

    /**
     * 模糊查询店内商品
     * @param shopId
     * @param key
     * @return
     */
    @GetMapping("/commodityLikeByShop")
    public R commodityLikeByShop(@RequestParam(value = "shopId") Integer shopId, @RequestParam(value = "key") String key) {
        return R.ok(commodityInfoService.commodityLikeByShop(shopId, key));
    }

    /**
     * 查找商品或店铺
     * @param key
     * @return
     */
    @GetMapping("/getGoodsFuzzy")
    public R getGoodsFuzzy(String key) {
        return R.ok(commodityInfoService.getGoodsFuzzy(key));
    }

    /**
     * 获取用户所有订单
     * @param userId
     * @return
     */
    @GetMapping("/getOrderListByUserId")
    public R getOrderListByUserId(Integer userId) {
        return R.ok(orderInfoService.getOrderListByUserId(userId));
    }

    /**
     * 订单收货
     * @param orderId
     * @return
     */
    @GetMapping("/receipt")
    public R receipt(Integer orderId) {
        return R.ok(orderInfoService.update(Wrappers.<OrderInfo>lambdaUpdate().set(OrderInfo::getOrderStatus, 3).eq(OrderInfo::getId, orderId)));
    }

    /**
     * 添加评价信息
     * @param evaluation
     * @return
     */
    @PostMapping("/evaluationAdd")
    public R evaluationAdd(@RequestBody Evaluation evaluation) {
        evaluation.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(evaluationService.save(evaluation));
    }

    /**
     * 获取商品评价信息
     * @param commodityId
     * @return
     */
    @GetMapping("/getEvaluationByGoods")
    public R getEvaluationByGoods(Integer commodityId) {
        return R.ok(evaluationService.getEvaluationByCommodityId(commodityId));
    }

    /**
     * 添加商品信息
     * @param commodityInfo
     * @return
     */
    @PostMapping("/addCommodity")
    public R addCommodity(@RequestBody CommodityInfo commodityInfo) {
        commodityInfo.setCode("C-"+new Date().getTime());
        commodityInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        ShopInfo shopInfo = shopInfoService.getOne(Wrappers.<ShopInfo>lambdaQuery().eq(ShopInfo::getUserId, commodityInfo.getShopId()));
        commodityInfo.setShopId(shopInfo.getId());
        return R.ok(commodityInfoService.save(commodityInfo));
    }

    /**
     * 修改商品信息
     * @param commodityInfo
     * @return
     */
    @PostMapping("/editCommodity")
    public R editCommodity(@RequestBody CommodityInfo commodityInfo) {
        return R.ok(commodityInfoService.updateById(commodityInfo));
    }

    /**
     * 根据用户获取商品信息
     * @param userId
     * @return
     */
    @GetMapping("/getGoodsByUserId")
    public R getGoodsByUserId(Integer userId) {
        return R.ok(commodityInfoService.getGoodsByUserId(userId));
    }

    /**
     * 根据ID获取商品信息
     * @param commodityId
     * @return
     */
    @GetMapping("/getGoodsInfoById")
    public R getGoodsInfoById(@RequestParam Integer commodityId) {
        return R.ok(commodityInfoService.getById(commodityId));
    }

    /**
     * 根据用户获取卖出的订单
     * @param userId
     * @return
     */
    @GetMapping("/getOrderByUserId")
    public R getOrderByUserId(@RequestParam Integer userId) {
        return R.ok(orderInfoService.getOrderByUserId(userId));
    }
}
