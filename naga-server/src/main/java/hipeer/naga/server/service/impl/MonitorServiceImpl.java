package hipeer.naga.server.service.impl;

import hipeer.naga.entity.cluster.HdfsEntity;
import hipeer.naga.entity.cluster.QueueMetricEntity;
import hipeer.naga.entity.cluster.YarnEntity;
import hipeer.naga.repository.cluster.HdfsRepository;
import hipeer.naga.repository.cluster.QueueMetricRepository;
import hipeer.naga.repository.cluster.YarnRepository;
import hipeer.naga.server.service.MonitorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MonitorServiceImpl implements MonitorService {

    @Resource
    HdfsRepository hdfsRepository;

    @Resource
    YarnRepository yarnRepository;

    @Resource
    QueueMetricRepository queueMetricRepository;

    @Override
    public void addHdfsSummary(HdfsEntity hdfsEntity) {
        hdfsRepository.save(hdfsEntity);
    }

    @Override
    public void addYarnSummary(YarnEntity yarnEntity) {
        yarnRepository.save(yarnEntity);
    }

    @Override
    public void addQueueMetric(List<QueueMetricEntity> queueMetricEntity) {
        queueMetricRepository.saveAll(queueMetricEntity);
    }

    @Override
    public HdfsEntity findHdfsSummary(String selectTime) {
        return hdfsRepository.findTop1ByIsTrashFalseAndCreateTimeLessThanEqualOrderByCreateTimeDesc(selectTime);
    }

    @Override
    public YarnEntity findYarnSummary(String selectTime) {
        return yarnRepository.findTop1ByIsTrashFalseAndCreateTimeLessThanEqualOrderByCreateTimeDesc(selectTime);
    }

    @Override
    public List<QueueMetricEntity> findQueueMetric(String selectTime) {
        return queueMetricRepository.findByCreateTime(selectTime);
    }

    @Override
    public List<HdfsEntity> findHdfsSummaryBetween(String startTime, String endTime) {
        return hdfsRepository.findByIsTrashFalseAndCreateTimeBetweenOrderByCreateTimeAsc(startTime, endTime);
    }

    @Override
    public List<YarnEntity> findYarnSummaryBetween(String startTime, String endTime) {
        return yarnRepository.findByIsTrashFalseAndCreateTimeBetweenOrderByCreateTimeAsc(startTime, endTime);
    }
}
