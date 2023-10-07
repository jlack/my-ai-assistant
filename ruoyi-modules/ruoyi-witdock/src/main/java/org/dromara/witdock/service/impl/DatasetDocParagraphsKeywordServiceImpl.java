package org.dromara.witdock.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.witdock.domain.bo.DatasetDocParagraphsKeywordBo;
import org.dromara.witdock.domain.vo.DatasetDocParagraphsKeywordVo;
import org.dromara.witdock.domain.DatasetDocParagraphsKeyword;
import org.dromara.witdock.mapper.DatasetDocParagraphsKeywordMapper;
import org.dromara.witdock.service.IDatasetDocParagraphsKeywordService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 段落关键字表Service业务层处理
 *
 * @author zyh
 * @date 2023-10-07
 */
@RequiredArgsConstructor
@Service
public class DatasetDocParagraphsKeywordServiceImpl implements IDatasetDocParagraphsKeywordService {

    private final DatasetDocParagraphsKeywordMapper baseMapper;

    /**
     * 查询段落关键字表
     */
    @Override
    public DatasetDocParagraphsKeywordVo queryById(Long paragraphsId){
        return baseMapper.selectVoById(paragraphsId);
    }

    /**
     * 查询段落关键字表列表
     */
    @Override
    public TableDataInfo<DatasetDocParagraphsKeywordVo> queryPageList(DatasetDocParagraphsKeywordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DatasetDocParagraphsKeyword> lqw = buildQueryWrapper(bo);
        Page<DatasetDocParagraphsKeywordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询段落关键字表列表
     */
    @Override
    public List<DatasetDocParagraphsKeywordVo> queryList(DatasetDocParagraphsKeywordBo bo) {
        LambdaQueryWrapper<DatasetDocParagraphsKeyword> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DatasetDocParagraphsKeyword> buildQueryWrapper(DatasetDocParagraphsKeywordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DatasetDocParagraphsKeyword> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getParagraphsId() != null, DatasetDocParagraphsKeyword::getParagraphsId, bo.getParagraphsId());
        lqw.eq(StringUtils.isNotBlank(bo.getKeyword()), DatasetDocParagraphsKeyword::getKeyword, bo.getKeyword());
        return lqw;
    }

    /**
     * 新增段落关键字表
     */
    @Override
    public Boolean insertByBo(DatasetDocParagraphsKeywordBo bo) {
        DatasetDocParagraphsKeyword add = MapstructUtils.convert(bo, DatasetDocParagraphsKeyword.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setParagraphsId(add.getParagraphsId());
        }
        return flag;
    }

    /**
     * 修改段落关键字表
     */
    @Override
    public Boolean updateByBo(DatasetDocParagraphsKeywordBo bo) {
        DatasetDocParagraphsKeyword update = MapstructUtils.convert(bo, DatasetDocParagraphsKeyword.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DatasetDocParagraphsKeyword entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除段落关键字表
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
