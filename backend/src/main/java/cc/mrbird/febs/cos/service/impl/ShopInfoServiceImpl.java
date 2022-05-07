package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.entity.ShopInfo;
import cc.mrbird.febs.cos.dao.ShopInfoMapper;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import cc.mrbird.febs.cos.service.IShopInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShopInfoServiceImpl extends ServiceImpl<ShopInfoMapper, ShopInfo> implements IShopInfoService {

    private final ICommodityInfoService commodityInfoService;

    @Override
    public IPage<LinkedHashMap<String, Object>> getShopInfoByPage(Page page, ShopInfo shopInfo) {
        return baseMapper.getShopInfoByPage(page, shopInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> shopInfoHot() {
        List<LinkedHashMap<String, Object>> shopList = baseMapper.shopInfoHot();
        shopList.forEach(item -> {
            item.put("commodityList", commodityInfoService.list(Wrappers.<CommodityInfo>lambdaQuery().eq(CommodityInfo::getShopId, Integer.parseInt(item.get("id").toString()))));
        });
        return shopList;
    }
}
