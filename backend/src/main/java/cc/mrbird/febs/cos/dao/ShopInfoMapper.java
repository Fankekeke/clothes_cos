package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ShopInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ShopInfoMapper extends BaseMapper<ShopInfo> {

    // 分页查询商铺信息
    IPage<LinkedHashMap<String, Object>> getShopInfoByPage(Page page, @Param("shopInfo") ShopInfo shopInfo);

    // 设计师信息
    List<LinkedHashMap<String, Object>> shopInfoHot();

}
