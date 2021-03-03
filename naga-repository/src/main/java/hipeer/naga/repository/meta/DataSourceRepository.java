package hipeer.naga.repository.meta;

import hipeer.naga.entity.meta.DataSourceInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataSourceRepository extends JpaRepository<DataSourceInfoEntity, Long> {

    DataSourceInfoEntity findByDataSourceName(String dataSourceName);

    List<DataSourceInfoEntity> findByProjectName(String projectName);

    List<DataSourceInfoEntity> findByProjectId(Long projectId);
}
