package hipeer.naga.entity.cluster;

import hipeer.naga.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;


import javax.persistence.*;

@Data
@Entity
@ToString
@Table(name = "naga_hdfs")
public class HdfsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // hdfs总空间
    private Long total;
    // hdfs已用空间
    private Long hdfsUsed;
    // hdfs已用半分比
    private float percentUsed;
    // hdfs空闲空间
    private Long hdfsFree;

    private Long hdfsNonUsed;
    // hdfs总块数
    private Long totalBlocks;
    // hdfs总文件数
    private Long totalFiles;
    // hdfs丢失的块数
    private Long missingBlocks;
    // 活跃的datanode节点
    private Integer liveDataNodeNums;
    // 死亡的datanode节点
    private Integer deadDataNodeNums;
    // 损坏的空间数量
    private Integer volumeFailuresTotal;
}
