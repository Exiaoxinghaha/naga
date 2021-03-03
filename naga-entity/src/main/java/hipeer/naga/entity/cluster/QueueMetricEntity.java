package hipeer.naga.entity.cluster;

import hipeer.naga.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "naga_queue_metric")
public class QueueMetricEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 队列名称
    private String queueName;
    // 队列运行的应用数量
    private Integer runningApps;
    // 队列挂起的应用数量
    private Integer pendingApps;
    // 已分配的内存
    private Long allocatedMemory;
    // 未分配的内存
    private Long reservedMemory;
    // 挂起的内存
    private Long pendingMemory;
    // 活跃的用户数量
    private Integer activeUsers;
    // 时间
    private String metricTime;

    private Integer allocatedContainers;

    private Integer pendingContainers;

    private Long AvailableMemory;
}
