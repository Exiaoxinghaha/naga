package hipeer.naga.server.service;

import hipeer.naga.entity.meta.DataBaseInfoEntity;
import hipeer.naga.entity.meta.DataSourceInfoEntity;
import hipeer.naga.entity.meta.ProjectInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

public interface MateService {

    // 创建业务线
    void createProjectInfo(ProjectInfoEntity projectInfoEntity) throws IOException, InterruptedException;
    // 删除业务线
    void deletcProjectInfo(Long id);
    // 更新业务线
    void updateProjectInfo(ProjectInfoEntity projectInfoEntity) throws IOException, InterruptedException;
    // 查询业务线
    ProjectInfoEntity findProjectInfoByName(String projectName);

    ProjectInfoEntity findProjectInfoById(Long projectId);

    List<String> findProjectInfoByTeam(String team);

    // 分页查询业务线
    Page<ProjectInfoEntity> findProjectInfoOfPage(String team, int page, int size, String sort, Sort.Direction direction);

    // 添加数据库信息
    void addDatabaseInfo(DataBaseInfoEntity dataBaseInfoEntity);
    // 删除数据库信息
    void deleteDatabaseInfo(Long dataBaseId);
    // 更新数据库信息
    void updateDataBaseInfo(DataBaseInfoEntity dataBaseInfoEntity);
    // 查询数据库信息
    DataBaseInfoEntity findDataBaseInfoByName(String dataBaseInfoName);

    DataBaseInfoEntity findDataBaseInfoById(Long dataBaseInfoId);

    List<DataBaseInfoEntity> findDataBaseInfoByProjectName(String projectName);

    List<DataBaseInfoEntity> findDataBaseInfoByProjectId(Long projectId);
    // 分页查询数据库信息
    Page<DataBaseInfoEntity> findDataBaseInfoOfPage(String team, int page, int size, String sort, Sort.Direction direction);

    // 添加数据源
    void addDataSource(DataSourceInfoEntity dataSourceInfoEntity);
    // 删除数据源
    void deleteDataSource(Long dataSourceId);
    // 更新数据源
    void updateDataSource(DataSourceInfoEntity dataSourceInfoEntity);
    // 查询数据源
    DataSourceInfoEntity findDataSourceInfoByName(String dataSourceName);

    DataSourceInfoEntity findDataSourceInfoById(Long dataSourceId);

    List<DataSourceInfoEntity> findDataSourceInfoByProjectName(String projectName);

    List<DataSourceInfoEntity> findDataSourceInfoByProjectId(Long projectId);
    // 分页查询数据源
    Page<DataSourceInfoEntity> findDataSourceInfoOfPage(String team, int page, int size, String sort, Sort.Direction direction);

}
