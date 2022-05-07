package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MessageInfo;
import cc.mrbird.febs.cos.dao.MessageInfoMapper;
import cc.mrbird.febs.cos.service.IMessageInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements IMessageInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getMessageByPage(Page page, MessageInfo messageInfo) {
        return baseMapper.getMessageByPage(page, messageInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> messageListById(Integer userId) {
        return baseMapper.messageListById(userId);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getMessageDetail(Integer takeUser, Integer sendUser) {
        return baseMapper.getMessageDetail(takeUser, sendUser);
    }
}
