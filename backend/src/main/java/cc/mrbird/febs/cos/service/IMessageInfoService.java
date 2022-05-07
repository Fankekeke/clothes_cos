package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MessageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IMessageInfoService extends IService<MessageInfo> {

    // 分页查询消息
    IPage<LinkedHashMap<String, Object>> getMessageByPage(Page page, MessageInfo messageInfo);

    // 查询消息信息
    List<LinkedHashMap<String, Object>> messageListById(Integer userId);

    // 查找聊天记录
    List<LinkedHashMap<String, Object>> getMessageDetail(Integer takeUser, Integer sendUser);
}
