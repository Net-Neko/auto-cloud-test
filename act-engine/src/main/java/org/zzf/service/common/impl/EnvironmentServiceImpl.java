package org.zzf.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zzf.enums.BizCodeEnum;
import org.zzf.exception.BizException;
import org.zzf.util.SpringBeanUtil;
import org.zzf.dto.common.EnvironmentDTO;
import org.zzf.mapper.EnvironmentMapper;
import org.zzf.mapper.ProjectMapper;
import org.zzf.model.EnvironmentDO;
import org.zzf.model.ProjectDO;
import org.zzf.req.common.EnvironmentSaveReq;
import org.zzf.req.common.EnvironmentUpdateReq;
import org.zzf.service.common.EnvironmentService;

import java.util.List;

/**
 * 环境管理服务实现类
 * 提供环境的增删改查等核心业务功能
 *
 * @author 詹泽峰
 * @date 2025/12/04 21:53
 */
@Service
@Slf4j
public class EnvironmentServiceImpl implements EnvironmentService {

    /**
     * 项目数据访问对象
     */
    @Resource
    private ProjectMapper projectMapper;

    /**
     * 环境数据访问对象
     */
    @Resource
    private EnvironmentMapper environmentMapper;

    /**
     * 根据项目ID获取环境列表
     *
     * @param projectId 项目ID
     * @return 环境列表
     * @throws BizException 当参数为空或查询失败时抛出业务异常
     */
    @Override
    public List<EnvironmentDTO> list(Long projectId) {
        // 参数校验
        if (projectId == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("项目ID不能为空"));
        }

        try {
            // 构建查询条件
            LambdaQueryWrapper<EnvironmentDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EnvironmentDO::getProjectId, projectId);

            // 执行查询
            List<EnvironmentDO> environmentDOList = environmentMapper.selectList(queryWrapper);

            // 转换为DTO对象并返回
            return SpringBeanUtil.copyProperties(environmentDOList, EnvironmentDTO.class);
        } catch (Exception e) {
            log.error("获取环境列表失败，projectId: {}", projectId, e);
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 保存新的环境配置
     *
     * @param req 环境保存请求对象
     * @return 插入成功的记录数
     * @throws BizException 当参数为空、项目不存在或保存失败时抛出业务异常
     */
    @Override
    public int save(EnvironmentSaveReq req) {
        // 参数校验
        if (req == null || req.getProjectId() == null || req.getName() == null || req.getProtocol() == null || req.getDomain() == null || req.getPort() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("请求参数不能为空"));
        }

        try {
            // 验证项目是否存在
            ProjectDO projectDO = projectMapper.selectById(req.getProjectId());
            if (projectDO != null) {
                // 转换为DO对象并插入数据库
                EnvironmentDO environmentDO = SpringBeanUtil.copyProperties(req, EnvironmentDO.class);
                int result = environmentMapper.insert(environmentDO);
                log.info("新增环境成功，environmentId: {}", environmentDO.getId());
                return result;
            } else {
                log.warn("新增环境失败，项目不存在，projectId: {}", req.getProjectId());
                throw new BizException(BizCodeEnum.DATA_NOT_FOUND, new IllegalArgumentException("项目不存在"));
            }
        } catch (Exception e) {
            log.error("新增环境失败，req: {}", req, e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 更新环境配置信息
     *
     * @param req 环境更新请求对象
     * @return 更新成功的记录数
     * @throws BizException 当参数校验失败、环境不存在或更新失败时抛出业务异常
     */
    @Override
    public int update(EnvironmentUpdateReq req) {
        // 参数校验
        validateUpdateRequest(req);
        try {
            // 验证项目是否存在
            ProjectDO projectDO = projectMapper.selectById(req.getProjectId());
            if (projectDO != null) {
                // 转换为DO对象并更新数据库
                EnvironmentDO environmentDO = SpringBeanUtil.copyProperties(req, EnvironmentDO.class);
                int result = environmentMapper.updateById(environmentDO);

                // 检查更新结果
                if (result == 0) {
                    log.warn("更新环境失败，未找到匹配记录，id: {}, projectId: {}", req.getId(), req.getProjectId());
                    throw new BizException(BizCodeEnum.DATA_NOT_FOUND);
                }

                log.info("更新环境成功，environmentId: {}", req.getId());
                return result;
            } else {
                log.warn("更新环境失败，项目不存在，projectId: {}", req.getProjectId());
                throw new BizException(BizCodeEnum.DATA_NOT_FOUND, new IllegalArgumentException("项目不存在"));
            }
        } catch (Exception e) {
            log.error("更新环境失败，req: {}", req, e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 根据ID和项目ID删除环境配置
     *
     * @param id 环境ID
     * @param projectId 项目ID
     * @return 删除成功的记录数
     * @throws BizException 当参数为空、环境不存在或删除失败时抛出业务异常
     */
    @Override
    public int delete(Long id, Long projectId) {
        // 参数校验
        if (id == null || projectId == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("参数不能为空"));
        }

        try {
            // 构建删除条件
            LambdaQueryWrapper<EnvironmentDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EnvironmentDO::getId, id);
            queryWrapper.eq(EnvironmentDO::getProjectId, projectId);

            // 执行删除操作
            int result = environmentMapper.delete(queryWrapper);

            // 检查删除结果
            if (result == 0) {
                log.warn("删除环境失败，未找到匹配记录，id: {}, projectId: {}", id, projectId);
                throw new BizException(BizCodeEnum.DATA_NOT_FOUND);
            }

            log.info("删除环境成功，environmentId: {}, projectId: {}", id, projectId);
            return result;
        } catch (Exception e) {
            log.error("删除环境失败，id: {}, projectId: {}", id, projectId, e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 更新请求参数校验
     * 校验更新请求中的必要参数是否完整有效
     *
     * @param req 压力测试用例更新请求对象
     * @throws BizException 当参数校验失败时抛出业务异常
     */
    private void validateUpdateRequest(EnvironmentUpdateReq req) {
        if (req == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("请求参数不能为空"));
        }

        if (req.getId() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("环境ID不能为空"));
        }

        if (req.getProjectId() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("项目ID不能为空"));
        }
    }
}
