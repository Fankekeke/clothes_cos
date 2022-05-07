package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.MessageInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface MessageInfoMapper extends BaseMapper<MessageInfo> {

    // 分页查询消息
    IPage<LinkedHashMap<String, Object>> getMessageByPage(Page page, @Param("messageInfo") MessageInfo messageInfo);

    // 查询消息信息
    List<LinkedHashMap<String, Object>> messageListById(@Param("userId") Integer userId);

    // 查找聊天记录
    List<LinkedHashMap<String, Object>> getMessageDetail(@Param("takeUser") Integer takeUser, @Param("sendUser") Integer sendUser);
}
