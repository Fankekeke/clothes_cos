package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AuditInfo;
import cc.mrbird.febs.cos.dao.AuditInfoMapper;
import cc.mrbird.febs.cos.service.IAuditInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class AuditInfoServiceImpl extends ServiceImpl<AuditInfoMapper, AuditInfo> implements IAuditInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getAuditInfoByPage(Page page, AuditInfo auditInfo) {
        return baseMapper.getAuditInfoByPage(page, auditInfo);
    }
}
