package hipeer.naga.repository.cluster;

import hipeer.naga.entity.cluster.HdfsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HdfsRepository extends JpaRepository<HdfsEntity, Long> {

    HdfsEntity findTop1ByIsTrashFalseAndCreateTimeLessThanEqualOrderByCreateTimeDesc(String selectTime);

    List<HdfsEntity> findByIsTrashFalseAndCreateTimeBetweenOrderByCreateTimeAsc(String startTime, String endTime);

}
