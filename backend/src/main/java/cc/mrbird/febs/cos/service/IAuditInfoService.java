package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.AuditInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IAuditInfoService extends IService<AuditInfo> {

    // 分页获取审核信息
    IPage<LinkedHashMap<String, Object>> getAuditInfoByPage(Page page, AuditInfo auditInfo);
}
