package hipeer.naga.repository.meta;

import hipeer.naga.entity.meta.ProjectInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, Long> {
    ProjectInfoEntity findByProjectName(String projectName);
}
