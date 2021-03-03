package hipeer.naga.repository.cluster;

import hipeer.naga.entity.cluster.YarnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YarnRepository extends JpaRepository<YarnEntity, Long> {

    YarnEntity findTop1ByIsTrashFalseAndCreateTimeLessThanEqualOrderByCreateTimeDesc(String selectTime);

    List<YarnEntity> findByIsTrashFalseAndCreateTimeBetweenOrderByCreateTimeAsc(String startTime, String endTime);

}
