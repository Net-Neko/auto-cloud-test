package org.zzf.service.stress.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zzf.enums.BizCodeEnum;
import org.zzf.exception.BizException;
import org.zzf.util.SpringBeanUtil;
import org.zzf.dto.stress.StressCaseDTO;
import org.zzf.dto.stress.StressCaseModuleDTO;
import org.zzf.mapper.StressCaseMapper;
import org.zzf.mapper.StressCaseModuleMapper;
import org.zzf.model.StressCaseDO;
import org.zzf.model.StressCaseModuleDO;
import org.zzf.req.stress.StressCaseModuleSaveReq;
import org.zzf.req.stress.StressCaseModuleUpdateReq;
import org.zzf.service.stress.StressCaseModuleService;

import java.util.List;

/**
 * 压力测试用例模块服务实现类
 * 提供压力测试用例模块的增删改查等核心业务功能
 *
 * @author 詹泽峰
 * @date 2025/12/21 21:58
 */
@Service
@Slf4j
public class StressCaseModuleServiceImpl implements StressCaseModuleService {

    @Resource
    private StressCaseMapper stressCaseMapper;

    @Resource
    private StressCaseModuleMapper stressCaseModuleMapper;

    /**
     * 根据项目ID获取压力测试用例模块列表
     *
     * @param projectId 项目ID
     * @return 压力测试用例模块DTO对象列表
     * @throws BizException 当项目ID为空时抛出业务异常
     */
    @Override
    public List<StressCaseModuleDTO> list(Long projectId) {
        // 参数校验
        if (projectId == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("项目ID不能为空"));
        }
        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, projectId);
        List<StressCaseModuleDO> stressCaseModuleDOList = stressCaseModuleMapper.selectList(queryWrapper);
        List<StressCaseModuleDTO> list = SpringBeanUtil.copyProperties(stressCaseModuleDOList, StressCaseModuleDTO.class);
        list.forEach(source -> {
            // 查询压测模型下的关联用例
            LambdaQueryWrapper<StressCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>();
            caseQueryWrapper.eq(StressCaseDO::getModuleId, source.getId()).orderByDesc(StressCaseDO::getId);

            List<StressCaseDO> stressCaseDOList = stressCaseMapper.selectList(caseQueryWrapper);
            List<StressCaseDTO> stressCaseDTOList = SpringBeanUtil.copyProperties(stressCaseDOList, StressCaseDTO.class);
            source.setList(stressCaseDTOList);
        });
        return list;
    }

    /**
     * 根据项目ID和模块ID查询压力测试用例模块详情
     *
     * @param projectId 项目ID
     * @param moduleId 模块ID
     * @return 压力测试用例模块DTO对象
     * @throws BizException 当参数为空时抛出业务异常
     */
    @Override
    public StressCaseModuleDTO findById(Long projectId, Long moduleId) {
        if (projectId == null || moduleId == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("参数不能为空"));
        }

        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, projectId).eq(StressCaseModuleDO::getId, moduleId);
        StressCaseModuleDO stressCaseModuleDO = stressCaseModuleMapper.selectOne(queryWrapper);
        if (stressCaseModuleDO == null) {
            return null;
        }
        StressCaseModuleDTO stressCaseModuleDTO = SpringBeanUtil.copyProperties(stressCaseModuleDO, StressCaseModuleDTO.class);

        // 查询压测模型下的关联用例
        LambdaQueryWrapper<StressCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>();
        caseQueryWrapper.eq(StressCaseDO::getModuleId, moduleId).orderByDesc(StressCaseDO::getId);
        List<StressCaseDO> stressCaseDOList = stressCaseMapper.selectList(caseQueryWrapper);
        List<StressCaseDTO> stressCaseDTOList = SpringBeanUtil.copyProperties(stressCaseDOList, StressCaseDTO.class);
        stressCaseModuleDTO.setList(stressCaseDTOList);
        return stressCaseModuleDTO;
    }

    /**
     * 根据项目ID和模块ID删除压力测试用例模块
     *
     * @param projectId 项目ID
     * @param id 模块ID
     * @return 删除成功的记录数
     * @throws BizException 当参数为空或删除失败时抛出业务异常
     */
    @Override
    public int delete(Long projectId, Long id) {
        if (id == null || projectId == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("参数不能为空"));
        }

        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, projectId).eq(StressCaseModuleDO::getId, id);
        int delete = stressCaseModuleMapper.delete(queryWrapper);
        if (delete > 0) {
            LambdaQueryWrapper<StressCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>();
            caseQueryWrapper.eq(StressCaseDO::getModuleId, id);
            stressCaseMapper.delete(caseQueryWrapper);
        }
        return delete;
    }

    /**
     * 保存新的压力测试用例模块
     *
     * @param req 压力测试用例模块保存请求对象
     * @return 插入成功的记录数
     * @throws BizException 当参数为空或保存失败时抛出业务异常
     */
    @Override
    public int save(StressCaseModuleSaveReq req) {
        if (req == null || req.getProjectId() == null || req.getName() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("参数不能为空"));
        }
        StressCaseModuleDO stressCaseModuleDO = SpringBeanUtil.copyProperties(req, StressCaseModuleDO.class);
        return stressCaseModuleMapper.insert(stressCaseModuleDO);
    }

    /**
     * 更新压力测试用例模块信息
     *
     * @param req 压力测试用例模块更新请求对象
     * @return 更新成功的记录数
     * @throws BizException 当参数校验失败或更新失败时抛出业务异常
     */
    @Override
    public int update(StressCaseModuleUpdateReq req) {
        if (req == null || req.getProjectId() == null || req.getId() == null || req.getName() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("参数不能为空"));
        }
        StressCaseModuleDO stressCaseModuleDO = SpringBeanUtil.copyProperties(req, StressCaseModuleDO.class);
        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, req.getProjectId()).eq(StressCaseModuleDO::getId, req.getId());
        return stressCaseModuleMapper.update(stressCaseModuleDO, queryWrapper);
    }
}
