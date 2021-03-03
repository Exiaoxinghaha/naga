package hipeer.naga.server.service;

import hipeer.naga.entity.cluster.HdfsEntity;
import hipeer.naga.entity.cluster.QueueMetricEntity;
import hipeer.naga.entity.cluster.YarnEntity;

import java.util.List;

public interface MonitorService {

    // 添加hdfs
    void addHdfsSummary(HdfsEntity hdfsEntity);

    // 添加yarn
    void addYarnSummary(YarnEntity yarnEntity);

    // 添加queue metric
    void addQueueMetric(List<QueueMetricEntity> queueMetricEntity);

    // 根据时间查找最近的hdfs情况
    HdfsEntity findHdfsSummary(String selectTime);

    // 根据时间查找最近的yarn情况
    YarnEntity findYarnSummary(String selectTime);

    // 根据时间查找queue metric
    List<QueueMetricEntity> findQueueMetric(String selectTime);

    // 查询某段时间的hdfs情况
    List<HdfsEntity> findHdfsSummaryBetween(String startTime, String endTime);

    // 查询某段时间的yarn情况
    List<YarnEntity> findYarnSummaryBetween(String startTime, String endTime);

}
