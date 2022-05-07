package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.AuditInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface AuditInfoMapper extends BaseMapper<AuditInfo> {

    // 分页获取审核信息
    IPage<LinkedHashMap<String, Object>> getAuditInfoByPage(Page page, @Param("auditInfo") AuditInfo auditInfo);
}
