package hipeer.naga.server.service.impl;

import cn.hutool.core.util.ObjectUtil;
import hipeer.naga.HadoopClient;
import hipeer.naga.entity.meta.DataBaseInfoEntity;
import hipeer.naga.entity.meta.DataSourceInfoEntity;
import hipeer.naga.entity.meta.ProjectInfoEntity;
import hipeer.naga.repository.meta.DataBasesInfoRepository;
import hipeer.naga.repository.meta.DataSourceRepository;
import hipeer.naga.repository.meta.ProjectInfoRepository;
import hipeer.naga.server.service.MateService;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.client.HdfsAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateServiceImpl implements MateService {

    @Value("${custom.hadoop.proxyUser}")
    private String proxyUser;

    @Value("${custom.hadoop.hivematestore}")
    private String hiveMateStoreUri;

    @Value("${custom.hadoop.conf}")
    private String hadoopConfPath;

    @Resource
    ProjectInfoRepository projectInfoRepository;

    @Resource
    DataBasesInfoRepository dataBasesInfoRepository;

    @Resource
    DataSourceRepository dataSourceRepository;

    @Override
    public void createProjectInfo(ProjectInfoEntity projectInfoEntity) throws IOException, InterruptedException {
        // 创建hdfs目录
        String hdfsUri = String.format("hdfs://%s", projectInfoEntity.getProjectNameSpace());
        HadoopClient hadoopClient = new HadoopClient(proxyUser, hadoopConfPath, hiveMateStoreUri);
        FileSystem fs = hadoopClient.getHadoopFileSystem(null, hdfsUri);
        if(!fs.exists(new Path(projectInfoEntity.getProjectHdfsPath()))){
            fs.mkdirs(new Path(projectInfoEntity.getProjectHdfsPath()));
        }
        // 设置配额
        HdfsAdmin admin = hadoopClient.getHdfsAdmin(hdfsUri);
        admin.setQuota(new Path(projectInfoEntity.getProjectHdfsPath()), projectInfoEntity.getProjectDataSpaceQuota());
        admin.setSpaceQuota(new Path(projectInfoEntity.getProjectHdfsPath()), projectInfoEntity.getProjectFileNumsQuota());
        projectInfoRepository.save(projectInfoEntity);
    }

    @Override
    public void deletcProjectInfo(Long id) {
        ProjectInfoEntity projectInfoEntity = projectInfoRepository.findById(id).get();
        projectInfoEntity.setIsTrash(true);
        projectInfoRepository.save(projectInfoEntity);
    }

    @Override
    public void updateProjectInfo(ProjectInfoEntity projectInfoEntity) throws IOException, InterruptedException {
        // 设置hdfs 配额
        String hdfsUri = String.format("hdfs://%s", projectInfoEntity.getProjectNameSpace());
        HadoopClient hadoopClient = new HadoopClient(proxyUser, hadoopConfPath, hiveMateStoreUri);
        HdfsAdmin hdfsAdmin = hadoopClient.getHdfsAdmin(hdfsUri);
        hdfsAdmin.setQuota(new Path(projectInfoEntity.getProjectHdfsPath()), projectInfoEntity.getProjectDataSpaceQuota());
        hdfsAdmin.setSpaceQuota(new Path(projectInfoEntity.getProjectHdfsPath()), projectInfoEntity.getProjectFileNumsQuota());
        projectInfoRepository.save(projectInfoEntity);
    }

    @Override
    public ProjectInfoEntity findProjectInfoByName(String projectName) {
        return projectInfoRepository.findByProjectName(projectName);
    }

    @Override
    public ProjectInfoEntity findProjectInfoById(Long projectId) {
        return projectInfoRepository.findById(projectId).get();
    }

    @Override
    public List<String> findProjectInfoByTeam(String team) {
        ProjectInfoEntity projectInfoEntity = new ProjectInfoEntity();
        projectInfoEntity.setIsTrash(false);
        projectInfoEntity.setTeam(team);
        return projectInfoRepository.findAll(Example.of(projectInfoEntity))
                .stream()
                .map(ProjectInfoEntity::getProjectName)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProjectInfoEntity> findProjectInfoOfPage(String team, int page, int size, String sort, Sort.Direction direction) {
        ProjectInfoEntity projectInfoEntity = new ProjectInfoEntity();
        projectInfoEntity.setIsTrash(false);
        projectInfoEntity.setTeam(team);
        return projectInfoRepository.findAll(Example.of(projectInfoEntity),
                PageRequest.of(page, size, Sort.by(direction == null? Sort.Direction.DESC: direction,
                        ObjectUtil.isNull(sort)? "id": sort)));
    }

    @Override
    public void addDatabaseInfo(DataBaseInfoEntity dataBaseInfoEntity) {
        // 创建hive数据库

        dataBasesInfoRepository.save(dataBaseInfoEntity);
    }

    @Override
    public void deleteDatabaseInfo(Long dataBaseId) {
        DataBaseInfoEntity dataBaseInfoEntity = dataBasesInfoRepository.findById(dataBaseId).get();
        dataBaseInfoEntity.setIsTrash(true);
        dataBasesInfoRepository.save(dataBaseInfoEntity);
    }

    @Override
    public void updateDataBaseInfo(DataBaseInfoEntity dataBaseInfoEntity) {
        dataBasesInfoRepository.save(dataBaseInfoEntity);
    }

    @Override
    public DataBaseInfoEntity findDataBaseInfoByName(String dataBaseInfoName) {
        return dataBasesInfoRepository.findByDataBaseName(dataBaseInfoName);
    }

    @Override
    public DataBaseInfoEntity findDataBaseInfoById(Long dataBaseInfoId) {
        return dataBasesInfoRepository.findById(dataBaseInfoId).get();
    }

    @Override
    public List<DataBaseInfoEntity> findDataBaseInfoByProjectName(String projectName) {
        return dataBasesInfoRepository.findByProjectName(projectName);
    }

    @Override
    public List<DataBaseInfoEntity> findDataBaseInfoByProjectId(Long projectId) {
        return dataBasesInfoRepository.findByProjectId(projectId);
    }

    @Override
    public Page<DataBaseInfoEntity> findDataBaseInfoOfPage(String team, int page, int size, String sort, Sort.Direction direction) {
        DataBaseInfoEntity dataBaseInfoEntity = new DataBaseInfoEntity();
        dataBaseInfoEntity.setIsTrash(false);
        dataBaseInfoEntity.setTeam(team);
        return dataBasesInfoRepository.findAll(Example.of(dataBaseInfoEntity),
                PageRequest.of(page, size, Sort.by(direction == null ? Sort.Direction.DESC: direction,
                        ObjectUtil.isNull(sort)? "id": sort)));
    }

    @Override
    public void addDataSource(DataSourceInfoEntity dataSourceInfoEntity) {
        dataSourceRepository.save(dataSourceInfoEntity);
    }

    @Override
    public void deleteDataSource(Long dataSourceId) {
        DataSourceInfoEntity dataSourceInfoEntity = dataSourceRepository.findById(dataSourceId).get();
        dataSourceInfoEntity.setIsTrash(true);
        dataSourceRepository.save(dataSourceInfoEntity);
    }

    @Override
    public void updateDataSource(DataSourceInfoEntity dataSourceInfoEntity) {
        dataSourceRepository.save(dataSourceInfoEntity);
    }

    @Override
    public DataSourceInfoEntity findDataSourceInfoByName(String dataSourceName) {
        return dataSourceRepository.findByDataSourceName(dataSourceName);
    }

    @Override
    public DataSourceInfoEntity findDataSourceInfoById(Long dataSourceId) {
        return dataSourceRepository.findById(dataSourceId).get();
    }

    @Override
    public List<DataSourceInfoEntity> findDataSourceInfoByProjectName(String projectName) {
        return dataSourceRepository.findByProjectName(projectName);
    }

    @Override
    public List<DataSourceInfoEntity> findDataSourceInfoByProjectId(Long projectId) {
        return dataSourceRepository.findByProjectId(projectId);
    }

    @Override
    public Page<DataSourceInfoEntity> findDataSourceInfoOfPage(String team, int page, int size, String sort, Sort.Direction direction) {
        DataSourceInfoEntity dataSourceInfoEntity = new DataSourceInfoEntity();
        dataSourceInfoEntity.setIsTrash(false);
        dataSourceInfoEntity.setTeam(team);
        return dataSourceRepository.findAll(Example.of(dataSourceInfoEntity),
                PageRequest.of(page, size, Sort.by(direction == null ? Sort.Direction.DESC: direction,
                        ObjectUtil.isNull(sort)? "id": sort)));
    }
}
