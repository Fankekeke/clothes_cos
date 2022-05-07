package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Evaluation;
import cc.mrbird.febs.cos.dao.EvaluationMapper;
import cc.mrbird.febs.cos.service.IEvaluationService;
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
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements IEvaluationService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getEvaluationPage(Page page, Evaluation evaluation) {
        return baseMapper.getEvaluationPage(page, evaluation);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getEvaluationByOrderId(Integer id) {
        return baseMapper.getEvaluationByOrderId(id);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getEvaluationByCommodityId(Integer id) {
        return baseMapper.getEvaluationByCommodityId(id);
    }
}
