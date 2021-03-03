package hipeer.naga.repository.meta;

import hipeer.naga.entity.meta.DataBaseInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataBasesInfoRepository extends JpaRepository<DataBaseInfoEntity, Long> {

    DataBaseInfoEntity findByDataBaseName(String databaseName);

    List<DataBaseInfoEntity> findByProjectName(String projectName);

    List<DataBaseInfoEntity> findByProjectId(Long projectId);
}
