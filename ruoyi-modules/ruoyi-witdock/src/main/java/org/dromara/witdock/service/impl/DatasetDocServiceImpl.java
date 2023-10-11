package org.dromara.witdock.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.witdock.domain.DatasetDoc;
import org.dromara.witdock.domain.bo.DatasetDocBo;
import org.dromara.witdock.domain.vo.DatasetDocVo;
import org.dromara.witdock.domain.vo.DatasetInfoVo;
import org.dromara.witdock.mapper.DatasetDocMapper;
import org.dromara.witdock.service.IDatasetDocService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 数据集文档Service业务层处理
 *
 * @author Lion Li
 * @date 2023-09-21
 */
@RequiredArgsConstructor
@Service
public class DatasetDocServiceImpl implements IDatasetDocService {

    private final DatasetDocMapper baseMapper;


    @Override
    public Integer countCharNumById(Long id) {
        return baseMapper.countCharNumByDatasetId(id);
    }


    /**
     * 查询数据集文档
     */
    @Override
    public DatasetDocVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询数据集文档
     */
    @Override
    public DatasetDocVo queryByOssId(Long ossId){
        return baseMapper.selectVoByOssId(ossId);
    }

    /**
     * 查询数据集文档列表
     */
    @Override
    public TableDataInfo<DatasetDocVo> queryPageList(DatasetDocBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DatasetDoc> lqw = buildQueryWrapper(bo);
        Page<DatasetDocVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询数据集文档列表
     */
    @Override
    public List<DatasetDocVo> queryList(DatasetDocBo bo) {
        LambdaQueryWrapper<DatasetDoc> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DatasetDoc> buildQueryWrapper(DatasetDocBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DatasetDoc> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDatasetId() != null, DatasetDoc::getDatasetId, bo.getDatasetId());
        lqw.like(StringUtils.isNotBlank(bo.getDocName()), DatasetDoc::getDocName, bo.getDocName());
        lqw.eq(bo.getOssId() != null, DatasetDoc::getOssId, bo.getOssId());
        lqw.eq(bo.getCharNum() != null, DatasetDoc::getCharNum, bo.getCharNum());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), DatasetDoc::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增数据集文档
     */
    @Override
    public Boolean insertByBo(DatasetDocBo bo) {
        DatasetDoc add = MapstructUtils.convert(bo, DatasetDoc.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改数据集文档
     */
    @Override
    public Boolean updateByBo(DatasetDocBo bo) {
        DatasetDoc update = MapstructUtils.convert(bo, DatasetDoc.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DatasetDoc entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除数据集文档
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
