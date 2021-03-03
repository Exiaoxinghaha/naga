package hipeer.naga.repository.cluster;

import hipeer.naga.entity.cluster.QueueMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueMetricRepository extends JpaRepository<QueueMetricEntity, Long> {

    List<QueueMetricEntity> findByCreateTime(String selectTime);
}
