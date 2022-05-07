package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AddressInfo;
import cc.mrbird.febs.cos.service.IAddressInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/address-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressInfoController {

    private final IAddressInfoService addressInfoService;

    /**
     * 分页获取收货地址信息
     * @param page
     * @param addressInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, AddressInfo addressInfo) {
        return R.ok(addressInfoService.getAddressInfoByPage(page, addressInfo));
    }

    /**
     * 删除收货地址信息
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(addressInfoService.removeByIds(ids));
    }

}
