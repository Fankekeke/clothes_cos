package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    // 分页查询订单信息
    IPage<LinkedHashMap<String, Object>> getOrderByPage(Page page, @Param("orderInfo") OrderInfo orderInfo);

    // 根据用户ID获取订单
    List<LinkedHashMap<String, Object>> orderListByUserId(@Param("userId") Integer userId);

    // 订单统计
    List<LinkedHashMap<String, Object>> orderRevenueData();

    // 本月订单量 本月收益
    LinkedHashMap<String, Object> orderMonthRevenue();

    // 本月收益占比
    List<LinkedHashMap<String, Object>> orderPriceRate();

    // 商铺收益订单统计
    List<LinkedHashMap<String, Object>> shopOrderRate();

    // 根据商铺获取订单统计
    List<LinkedHashMap<String, Object>> shopOrderRateByComm(@Param("shopId") Integer shopId);

    // 所有店铺信息
    List<LinkedHashMap<String, Object>> shopList();

    // 查询用户购物车
    List<LinkedHashMap<String, Object>> selGoodsCart(@Param("userId") Integer userId);

    // 根据订单ID获取订单信息
    List<LinkedHashMap<String, Object>> selOrderListByOrderIds(@Param("ids") List<String> ids);

    // 获取用户所有订单
    List<LinkedHashMap<String, Object>> getOrderListByUserId(@Param("userId") Integer userId);

    // 根据用户获取卖出的订单
    List<LinkedHashMap<String, Object>> getOrderByUserId(@Param("userId") Integer userId);
}
