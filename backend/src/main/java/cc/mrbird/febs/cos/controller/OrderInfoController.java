package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoController {

    private final IOrderInfoService orderInfoService;

    /**
     * 获取主页数据
     * @return
     */
    @GetMapping("/home")
    public R home() {
        return R.ok(orderInfoService.home());
    }

    /**
     * 根据商铺获取订单统计
     * @param shopId
     * @return
     */
    @GetMapping("/shopOrderRateByComm")
    public R shopOrderRateByComm(@RequestParam Integer shopId) {
        return R.ok(orderInfoService.shopOrderRateByComm(shopId));
    }

    /**
     * 根据用户ID获取订单
     * @return
     */
    @GetMapping("/orderListByUserId")
    public R orderListByUserId(@RequestParam Integer userId) {
        return R.ok(orderInfoService.orderListByUserId(userId));
    }

    /**
     * 分页查询订单信息
     * @param page
     * @param orderInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, OrderInfo orderInfo) {
        return R.ok(orderInfoService.getOrderByPage(page, orderInfo));
    }

    /**
     * 删除订单信息
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderInfoService.removeByIds(ids));
    }

}
