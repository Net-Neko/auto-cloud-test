package org.zzf.service.common.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zzf.enums.BizCodeEnum;
import org.zzf.exception.BizException;
import org.zzf.util.SpringBeanUtil;
import org.zzf.dto.common.ProjectDTO;
import org.zzf.mapper.ProjectMapper;
import org.zzf.model.ProjectDO;
import org.zzf.req.common.ProjectSaveReq;
import org.zzf.req.common.ProjectUpdateReq;
import org.zzf.service.common.ProjectService;

import java.util.List;

/**
 * 项目服务实现类
 *
 * @author 詹泽峰
 * @date 2025/12/04 17:10
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    /**
     * 获取项目列表
     * 查询所有项目信息并转换为DTO对象返回
     *
     * @return 项目DTO列表
     */
    @Override
    public List<ProjectDTO> list() {
        List<ProjectDO> projectDOList = projectMapper.selectList(null);
        List<ProjectDTO> projectDTOList = SpringBeanUtil.copyProperties(projectDOList, ProjectDTO.class);
        return projectDTOList;
    }

    /**
     * 保存项目信息
     *
     * @param req 项目保存请求参数对象，包含要保存的项目信息
     * @return 返回插入操作影响的记录数，通常为1表示插入成功
     * @throws BizException 当参数为空或保存失败时抛出业务异常
     */
    @Override
    public int save(ProjectSaveReq req) {
        if (req == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("请求参数不能为空"));
        }

        try {
            ProjectDO projectDO = SpringBeanUtil.copyProperties(req, ProjectDO.class);
            int result = projectMapper.insert(projectDO);
            return result;
        } catch (Exception e) {
            log.error("保存项目信息失败，req: {}", req, e);
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 更新项目信息
     *
     * @param req 项目更新请求对象，包含需要更新的项目信息
     * @return 更新影响的记录数
     * @throws BizException 当参数校验失败、项目不存在或更新失败时抛出业务异常
     */
    @Override
    public int update(ProjectUpdateReq req) {
        // 参数校验
        validateUpdateRequest(req);

        try {
            ProjectDO projectDO = SpringBeanUtil.copyProperties(req, ProjectDO.class);
            int result = projectMapper.updateById(projectDO);

            if (result == 0) {
                log.warn("更新项目信息失败，未找到匹配记录，id: {}", req.getId());
                throw new BizException(BizCodeEnum.DATA_NOT_FOUND);
            }

            return result;
        } catch (Exception e) {
            log.error("更新项目信息失败，req: {}", req, e);
            if (e instanceof BizException) {
                throw e;
            }
            throw new BizException(BizCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 删除项目信息
     *
     * @param id 项目ID
     * @return 删除影响的记录数
     * @throws BizException 当参数为空、项目不存在或删除失败时抛出业务异常
     */
    @Override
    public int delete(Long id) {
        if (id == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("项目ID不能为空"));
        }

        try {
            int result = projectMapper.deleteById(id);

            if (result == 0) {
                log.warn("删除项目信息失败，未找到匹配记录，id: {}", id);
                throw new BizException(BizCodeEnum.DATA_NOT_FOUND);
            }

            return result;
        } catch (Exception e) {
            log.error("删除项目信息失败，id: {}", id, e);
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
     * @param req 项目更新请求对象
     * @throws BizException 当参数校验失败时抛出业务异常
     */
    private void validateUpdateRequest(ProjectUpdateReq req) {
        if (req == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("请求参数不能为空"));
        }

        if (req.getId() == null) {
            throw new BizException(BizCodeEnum.PARAM_ERROR, new IllegalArgumentException("ID不能为空"));
        }
    }
}
