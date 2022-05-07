package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AddressInfo;
import cc.mrbird.febs.cos.dao.AddressInfoMapper;
import cc.mrbird.febs.cos.service.IAddressInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class AddressInfoServiceImpl extends ServiceImpl<AddressInfoMapper, AddressInfo> implements IAddressInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getAddressInfoByPage(Page page, AddressInfo addressInfo) {
        return baseMapper.getAddressInfoByPage(page, addressInfo);
    }
}
