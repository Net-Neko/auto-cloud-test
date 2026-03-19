package org.zzf.service.stress.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zzf.dto.ReportDTO;
import org.zzf.enums.BizCodeEnum;
import org.zzf.enums.ReportStateEnum;
import org.zzf.enums.StressSourceTypeEnum;
import org.zzf.enums.TestTypeEnum;
import org.zzf.exception.BizException;
import org.zzf.feign.ReportFeignService;
import org.zzf.req.ReportSaveReq;
import org.zzf.util.JsonData;
import org.zzf.util.SpringBeanUtil;
import org.zzf.dto.stress.StressCaseDTO;
import org.zzf.mapper.StressCaseMapper;
import org.zzf.model.StressCaseDO;
import org.zzf.req.stress.StressCaseSaveReq;
import org.zzf.req.stress.StressCaseUpdateReq;
import org.zzf.service.stress.StressCaseService;

/**
 * 压力测试用例服务实现类
 * 提供压力测试用例的增删改查等核心业务功能
 *
 * @author 詹泽峰
 * @date 2025/12/17 21:19
 */
@Service
@Slf4j
public class StressCaseServiceImpl implements StressCaseService {

    @Resource
    private StressCaseMapper stressCaseMapper;
    @Autowired
    private ReportFeignService reportFeignService;

    /**
     * 根据项目ID和用例ID查询压力测试用例详情
     *
     * @param projectId 项目ID
     * @param caseId 用例ID
     * @return 压力测试用例DTO对象
     * @throws BizException 当参数为空或用例不存在时抛出业务异常
     */
    @Override
    public StressCaseDTO findById(Long projectId, Long caseId) {
        // 参数校验，业务简单无需分离
        if (projectId == null || caseId == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("参数不能为空"));
        }

        LambdaQueryWrapper<StressCaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseDO::getProjectId, projectId)
                .eq(StressCaseDO::getId, caseId);
        StressCaseDO stressCaseDO = stressCaseMapper.selectOne(queryWrapper);

        if (stressCaseDO == null) {
            throw new BizException(BizCodeEnum.DATA_NOT_FOUND);
        }

        return SpringBeanUtil.copyProperties(stressCaseDO, StressCaseDTO.class);
    }

    /**
     * 根据项目ID和用例ID删除压力测试用例
     *
     * @param id 用例ID
     * @param projectId 项目ID
     * @return 删除成功的记录数
     * @throws BizException 当参数为空、用例不存在或删除失败时抛出业务异常
     */
    @Override
    public int delete(Long id, Long projectId) {
        if (id == null || projectId == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("参数不能为空"));
        }

        try {
            LambdaQueryWrapper<StressCaseDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StressCaseDO::getId, id)
                    .eq(StressCaseDO::getProjectId, projectId);
            int result = stressCaseMapper.delete(queryWrapper);

            if (result == 0) {
                log.warn("删除压力测试用例失败，未找到匹配记录，id: {}, projectId: {}", id, projectId);
                throw new BizException(BizCodeEnum.DATA_NOT_FOUND);
            }

            return result;
        } catch (Exception e) {
            log.error("删除压力测试用例失败，id: {}, projectId: {}", id, projectId, e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 保存新的压力测试用例
     *
     * @param req 压力测试用例保存请求对象
     * @return 插入成功的记录数
     * @throws BizException 当参数为空或保存失败时抛出业务异常
     */
    @Override
    public int save(StressCaseSaveReq req) {
        if (req == null || req.getProjectId() == null || req.getModuleId() == null || req.getEnvironmentId() == null || req.getName() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("请求参数不能为空"));
        }

        try {
            StressCaseDO stressCaseDO = SpringBeanUtil.copyProperties(req, StressCaseDO.class);
            return stressCaseMapper.insert(stressCaseDO);
        } catch (Exception e) {
            log.error("保存压力测试用例失败，req: {}", req, e);
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 更新压力测试用例信息
     *
     * @param req 压力测试用例更新请求对象
     * @return 更新成功的记录数
     * @throws BizException 当参数校验失败、用例不存在或更新失败时抛出业务异常
     */
    @Override
    public int update(StressCaseUpdateReq req) {
        // 参数校验
        validateUpdateRequest(req);

        try {
            StressCaseDO stressCaseDO = SpringBeanUtil.copyProperties(req, StressCaseDO.class);
            LambdaQueryWrapper<StressCaseDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StressCaseDO::getId, req.getId())
                    .eq(StressCaseDO::getProjectId, req.getProjectId());
            int result = stressCaseMapper.update(stressCaseDO, queryWrapper);

            if (result == 0) {
                log.warn("更新压力测试用例失败，未找到匹配记录，id: {}, projectId: {}",
                        req.getId(), req.getProjectId());
                throw new BizException(BizCodeEnum.DATA_NOT_FOUND);
            }

            return result;
        } catch (Exception e) {
            log.error("更新压力测试用例失败，req: {}", req, e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 执行指定的压力测试用例
     * [1] 查询用例详情
     * [2] 初始化测试报告
     * [3] 判断压测类型，JMX、SIMPLE
     * [4] 初始化初始引擎
     * [5] 组装测试计划
     * [6] 执行压测
     * [7] 发送压测结果明细
     * [8] 压测完成清理数据
     * [9] 通知压测结束
     * TODO: 待实现具体执行逻辑
     *
     * @param projectId 项目ID
     * @param caseId 用例ID
     * @return 执行结果状态码
     */
    @Override
    public int execute(Long projectId, Long caseId) {
        // 查询用例详情
        LambdaQueryWrapper<StressCaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseDO::getId, caseId)
                .eq(StressCaseDO::getProjectId, projectId);
        StressCaseDO stressCaseDO = stressCaseMapper.selectOne(queryWrapper);
        // 初始化测试报告
        if (stressCaseDO != null) {
            ReportSaveReq reportSaveReq = ReportSaveReq.builder().projectId(stressCaseDO.getProjectId()).caseId(stressCaseDO.getId())
                    .startTime(System.currentTimeMillis())
                    .executeState(ReportStateEnum.EXECUTING.name())
                    .name(stressCaseDO.getName())
                    .type(TestTypeEnum.STRESS.name())
                    .build();
            // 检查测试报告是否初始化成功
            JsonData jsonData = reportFeignService.save(reportSaveReq);
            // 拿到测试报告主键id
            if(jsonData.isSuccess()) {
                ReportDTO reportDTO = jsonData.getData(ReportDTO.class);
                // 判断压测类型
                if (StressSourceTypeEnum.JMX.name().equalsIgnoreCase(stressCaseDO.getStressSourceType())) {

                } else if (StressSourceTypeEnum.SIMPLE.name().equalsIgnoreCase(stressCaseDO.getStressSourceType())) {

                } else {
                    throw new BizException(BizCodeEnum.STRESS_UNSUPPORTED);
                }
            }
        }
        return 0;
    }

    /**
     * 更新请求参数校验
     * 校验更新请求中的必要参数是否完整有效
     *
     * @param req 压力测试用例更新请求对象
     * @throws BizException 当参数校验失败时抛出业务异常
     */
    private void validateUpdateRequest(StressCaseUpdateReq req) {
        if (req == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("请求参数不能为空"));
        }

        if (req.getId() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("ID不能为空"));
        }

        if (req.getProjectId() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("项目ID不能为空"));
        }
    }

    private void runSimpleStressCase(StressCaseDO stressCaseDO, ReportDTO reportDTO) {

    }

    private void runJmxStressCase(StressCaseDO stressCaseDO, ReportDTO reportDTO) {

    }
}
