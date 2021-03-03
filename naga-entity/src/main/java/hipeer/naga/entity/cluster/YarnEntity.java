package hipeer.naga.entity.cluster;

import hipeer.naga.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "naga_yarn")
public class YarnEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 活跃的nodemanager数量
    private Integer liveNodeManagerNums;
    // 死亡的nodemanager数量
    private Integer deadNodeManagerNums;
    // 有问题的nodemanager数量
    private Integer unhealthyNodeManagerNums;
    // 提交的应用数量
    private Integer submmitApps;
    // 运行的应用数量
    private Integer runningApps;
    // 挂起的应用数量
    private Integer pendingApps;
    // 完成的任务数量
    private Integer completeApps;
    // 死亡的任务数量
    private Integer killedApps;
    // 失败的任务数量
    private Integer failedApps;
    // 分配的内存大小
    private Long allocatedMemory;
    // 分配的cpu核数
    private Integer allocatedCores;

    private Integer allocatedContainer;

    // 可用的内存大小
    private Long availableMemory;
    // 可用的cpu核数
    private Integer availableCores;
    // 挂起的内存
    private Long pendingMemory;
    // 挂起的cpu核数
    private Integer pendingCores;

    private Integer pendingContainers;
    // 保留的内存
    private Long reservedMemory;
    // 保留的cpu核数
    private Integer reservedCores;

    private Integer reservedContainers;
}
