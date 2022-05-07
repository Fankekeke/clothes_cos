package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    // 分页查询订单信息
    IPage<LinkedHashMap<String, Object>> getOrderByPage(Page page, OrderInfo orderInfo);

    // 根据用户ID获取订单
    List<LinkedHashMap<String, Object>> orderListByUserId(Integer userId);

    // 获取主页数据
    LinkedHashMap<String, Object> home();

    // 根据商铺获取订单统计
    List<LinkedHashMap<String, Object>> shopOrderRateByComm(Integer shopId);

    // 查询用户购物车
    List<LinkedHashMap<String, Object>> selGoodsCart(Integer userId);

    // 根据订单ID获取订单信息
    List<LinkedHashMap<String, Object>> selOrderListByOrderIds(List<String> ids);

    // 获取用户所有订单
    List<LinkedHashMap<String, Object>> getOrderListByUserId(Integer userId);

    // 根据用户获取卖出的订单
    List<LinkedHashMap<String, Object>> getOrderByUserId(@Param("userId") Integer userId);
}
