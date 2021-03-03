package hipeer.naga.server.controller;

import hipeer.naga.entity.meta.ProjectInfoEntity;
import hipeer.naga.exception.SystemConstants;
import hipeer.naga.server.BaseController;
import hipeer.naga.server.jwt.ContextUtil;
import hipeer.naga.server.service.MateService;
import hipeer.naga.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/naga/v1/mate")
public class MateController extends BaseController {

    @Autowired
    MateService mateService;

    // 创建业务线
    @ResponseBody
    @PostMapping("create_project")
    public Object createProject(@RequestBody ProjectInfoEntity projectInfoEntity) throws IOException, InterruptedException {
        projectInfoEntity.setIsTrash(false);
        projectInfoEntity.setTeam(ContextUtil.getCurrentUser().getUserTeam());
        projectInfoEntity.setAdmin(ContextUtil.getCurrentUser().getUserName());
        projectInfoEntity.setCreateTime(DateUtils.getNowDate());
        projectInfoEntity.setProjectDataSpaceQuota(projectInfoEntity.getProjectDataSpaceQuota());
        mateService.createProjectInfo(projectInfoEntity);
        return getSucessResult(SystemConstants.SYSTEM_SUCESS, "业务线创建成功");

    }
}
