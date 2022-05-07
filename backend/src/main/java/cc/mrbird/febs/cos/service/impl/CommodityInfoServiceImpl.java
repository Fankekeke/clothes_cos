package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.dao.CommodityInfoMapper;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class CommodityInfoServiceImpl extends ServiceImpl<CommodityInfoMapper, CommodityInfo> implements ICommodityInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getCommodityByPage(Page page, CommodityInfo commodityInfo) {
        return baseMapper.getCommodityByPage(page, commodityInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getCommodityHot() {
        return baseMapper.getCommodityHot();
    }

    @Override
    public LinkedHashMap<String, Object> goodsDetail(Integer commodityId) {
        return baseMapper.goodsDetail(commodityId);
    }

    @Override
    public List<LinkedHashMap<String, Object>> selShopDetailList() {
        List<LinkedHashMap<String, Object>> shopList = baseMapper.selShopList();
        shopList.forEach(item -> {
            item.put("goods", this.list(Wrappers.<CommodityInfo>lambdaQuery().eq(CommodityInfo::getShopId, item.get("id"))));
        });
        return shopList;
    }

    @Override
    public LinkedHashMap<String, Object> getShopDetail(Integer shopId) {
        LinkedHashMap<String, Object> result = baseMapper.shopInfoById(shopId);
        result.put("goods", baseMapper.shopInfoDetail(shopId));
        return result;
    }

    @Override
    public List<LinkedHashMap<String, Object>> shopCommoditySort(Integer shopId, Integer type) {
        return baseMapper.shopCommoditySort(shopId, type);
    }

    @Override
    public List<LinkedHashMap<String, Object>> commodityLikeByShop(Integer shopId, String key) {
        return baseMapper.commodityLikeByShop(shopId, key);
    }

    @Override
    public LinkedHashMap<String, Object> getGoodsFuzzy(String key) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("commodity", baseMapper.commodityLikeByShop(null, key));
        result.put("shop", baseMapper.shopInfoLike(key));
        return result;
    }

    @Override
    public List<LinkedHashMap<String, Object>> getGoodsByUserId(Integer userId) {
        return baseMapper.getGoodsByUserId(userId);
    }
}
