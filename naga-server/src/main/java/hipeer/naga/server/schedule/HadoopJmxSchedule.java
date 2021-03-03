package hipeer.naga.server.schedule;

import cn.hutool.core.date.DateUtil;
import hipeer.naga.entity.cluster.HdfsEntity;
import hipeer.naga.entity.cluster.QueueMetricEntity;
import hipeer.naga.entity.cluster.YarnEntity;
import hipeer.naga.server.service.MonitorService;
import hipeer.naga.utils.StatefulHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;


/**
 * 定时收集hdfs yarn queue jmx信息，存入mysql
 */
@Slf4j
@Component
public class HadoopJmxSchedule {

    @Value("${custom.hadoop.nn.uri}")
    private String nameNodeUri;

    @Value("${custom.hadoop.rm.uri}")
    private String resourceManagerUri;

    @Autowired
    MonitorService monitorService;

    private StatefulHttpClient statefulHttpClient = new StatefulHttpClient(null);

    public static final String JMXSERVERURLFORMAT = "http://%s/jmx?qry=%s";
    public static final String NAMENODEINFO = "Hadoop:service=NameNode,name=NameNodeInfo";
    public static final String FSNAMESYSTEM = "Hadoop:service=NameNode,name=FSNamesystem";
    public static final String FSNAMESYSTEMSTATE = "Hadoop:service=NameNode,name=FSNamesystemState";
    public static final String QUEUEMETRICS = "Hadoop:service=ResourceManager,name=QueueMetrics,q0=root";
    public static final String CLUSTERMETRICS = "Hadoop:service=ResourceManager,name=ClusterMetrics";

    public static final String QUEUEMETRICSALL = "Hadoop:service=ResourceManager,name=QueueMetrics,*";

    @Scheduled(cron = "*/30 * * * * ?")
    public void HadoopMetricCollectSchedule(){
        // 收集hdfs jmx
        HdfsEntity hdfsEntity = reportHdfsEntity(statefulHttpClient);
        if(hdfsEntity != null){
            monitorService.addHdfsSummary(hdfsEntity);
        }

        // 收集 yarn jmx
        YarnEntity yarnEntity = reportYarnEntity(statefulHttpClient);
        if(yarnEntity != null) {
            monitorService.addYarnSummary(yarnEntity);
        }

        // 收集队列 jmx
        List<QueueMetricEntity> queueMetricEntities = reportQueueMetricEntitys();
        monitorService.addQueueMetric(queueMetricEntities);
    }


    // 获取活跃的namenode url
    private String getActiveNameNodeUri(List<String> nameNodeUris) throws IOException {
        if(nameNodeUris.size() > 1){
            for(String nameNodeUri: nameNodeUris){
                String fsNameSystemUrl = String.format(JMXSERVERURLFORMAT, nameNodeUri, FSNAMESYSTEM);
                HadoopMetric hadoopMetric = statefulHttpClient.get(HadoopMetric.class, fsNameSystemUrl, null, null);
                if(hadoopMetric.getMetricBeanValue("tag.HAState").equals("active")){
                    return nameNodeUri;
                }
            }
        }
        return nameNodeUris.get(0);
    }

    // 获取活跃的resource manager url
    private String getActiveResourceManagerUri(List<String> resourceManagerUris) throws IOException {
        if(resourceManagerUris.size() > 1){
            for(String resourceManagerUri: resourceManagerUris){
                String clusterMetricUrl = String.format(JMXSERVERURLFORMAT, resourceManagerUri, CLUSTERMETRICS);
                HadoopMetric hadoopMetric = statefulHttpClient.get(HadoopMetric.class, clusterMetricUrl, null, null);
                if(hadoopMetric.getMetricBeanValue("tag.ClusterMetrics").toString().equals("ResourceManager")){
                    return resourceManagerUri;
                }
            }
        }
        return resourceManagerUris.get(0);
    }

    // 封装hdfs实体
    private HdfsEntity reportHdfsEntity(StatefulHttpClient statefulHttpClient){
        HdfsEntity hdfsEntity = new HdfsEntity();
        List<String> nameNodeUris = Arrays.asList(nameNodeUri.split(","));
        if(nameNodeUris.isEmpty()){
            hdfsEntity.setIsTrash(true);
            return hdfsEntity;
        }

        try{
            String activeNameNodeUri = getActiveNameNodeUri(nameNodeUris);
            String nameNodeInfoUrl = String.format(JMXSERVERURLFORMAT, activeNameNodeUri, NAMENODEINFO);
            HadoopMetric hadoopMetric = statefulHttpClient.get(HadoopMetric.class, nameNodeInfoUrl, null, null);
            hdfsEntity.setTotal(Long.parseLong(hadoopMetric.getMetricBeanValue("Total").toString()));
            hdfsEntity.setHdfsUsed(Long.parseLong(hadoopMetric.getMetricBeanValue("Used").toString()));
            hdfsEntity.setHdfsFree(Long.parseLong(hadoopMetric.getMetricBeanValue("Free").toString()));
            hdfsEntity.setPercentUsed(Float.parseFloat(hadoopMetric.getMetricBeanValue("PercentUsed").toString().substring(0, 5)));
            hdfsEntity.setHdfsNonUsed(Long.parseLong(hadoopMetric.getMetricBeanValue("NonDfsUsedSpace").toString()));
            hdfsEntity.setTotalBlocks(Long.parseLong(hadoopMetric.getMetricBeanValue("TotalBlocks").toString()));
            hdfsEntity.setMissingBlocks(Long.parseLong(hadoopMetric.getMetricBeanValue("NumberOfMissingBlocks").toString()));
            hdfsEntity.setTotalFiles(Long.parseLong(hadoopMetric.getMetricBeanValue("TotalFiles").toString()));

            String nameNodeMetricUrl = String.format(JMXSERVERURLFORMAT, activeNameNodeUri, FSNAMESYSTEMSTATE);
            HadoopMetric nameNodeMetric = statefulHttpClient.get(HadoopMetric.class, nameNodeMetricUrl, null, null);
            hdfsEntity.setLiveDataNodeNums((int)nameNodeMetric.getMetricBeanValue("NumLiveDataNodes"));
            hdfsEntity.setDeadDataNodeNums((int)nameNodeMetric.getMetricBeanValue("NumDeadDataNodes"));
            hdfsEntity.setVolumeFailuresTotal((int)nameNodeMetric.getMetricBeanValue("VolumeFailuresTotal"));

            hdfsEntity.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            log.error(e.getMessage());
            hdfsEntity.setIsTrash(true);
        }

        return hdfsEntity;
    }

    // 封装yarn实体
    private YarnEntity reportYarnEntity(StatefulHttpClient statefulHttpClient){
        YarnEntity yarnEntity = new YarnEntity();
        List<String> resourceManagerUris = Arrays.asList(resourceManagerUri.split(","));
        if(resourceManagerUris.isEmpty()){
            yarnEntity.setIsTrash(true);
            return yarnEntity;
        }
        try {
            String resourceManagerActiveUri = getActiveResourceManagerUri(resourceManagerUris);
            String clusterMetricUrl = String.format(JMXSERVERURLFORMAT, resourceManagerActiveUri, CLUSTERMETRICS);
            HadoopMetric clusterMetric = statefulHttpClient.get(HadoopMetric.class, clusterMetricUrl, null, null);
            yarnEntity.setLiveNodeManagerNums((int) clusterMetric.getMetricBeanValue("NumActiveNMs"));
            yarnEntity.setDeadNodeManagerNums((int) clusterMetric.getMetricBeanValue("NumLostNMs"));
            yarnEntity.setUnhealthyNodeManagerNums((int) clusterMetric.getMetricBeanValue("NumUnhealthyNMs"));

            String queueMetricUrl = String.format(JMXSERVERURLFORMAT, resourceManagerActiveUri, QUEUEMETRICS);
            HadoopMetric queueMetric = statefulHttpClient.get(HadoopMetric.class, queueMetricUrl, null, null);
            yarnEntity.setSubmmitApps((int)queueMetric.getMetricBeanValue("AppsSubmitted"));
            yarnEntity.setRunningApps((int)queueMetric.getMetricBeanValue("AppsRunning"));
            yarnEntity.setPendingApps((int)queueMetric.getMetricBeanValue("AppsPending"));
            yarnEntity.setCompleteApps((int)queueMetric.getMetricBeanValue("AppsCompleted"));
            yarnEntity.setKilledApps((int)queueMetric.getMetricBeanValue("AppsKilled"));
            yarnEntity.setFailedApps((int)queueMetric.getMetricBeanValue("AppsFailed"));
            yarnEntity.setAllocatedMemory(Long.parseLong(queueMetric.getMetricBeanValue("AllocatedMB").toString()));
            yarnEntity.setAllocatedCores((int)queueMetric.getMetricBeanValue("AllocatedVCores"));
            yarnEntity.setAllocatedContainer((int)queueMetric.getMetricBeanValue("AllocatedContainers"));
            yarnEntity.setAvailableMemory(Long.parseLong(queueMetric.getMetricBeanValue("AvailableMB").toString()));
            yarnEntity.setAvailableCores((int)queueMetric.getMetricBeanValue("AvailableVCores"));
            yarnEntity.setPendingMemory(Long.parseLong(queueMetric.getMetricBeanValue("PendingMB").toString()));
            yarnEntity.setPendingCores((int)queueMetric.getMetricBeanValue("PendingVCores"));
            yarnEntity.setPendingContainers((int)queueMetric.getMetricBeanValue("PendingContainers"));
            yarnEntity.setReservedMemory(Long.parseLong(queueMetric.getMetricBeanValue("ReservedMB").toString()));
            yarnEntity.setReservedCores((int)queueMetric.getMetricBeanValue("ReservedVCores"));
            yarnEntity.setReservedContainers((int)queueMetric.getMetricBeanValue("ReservedContainers"));

            yarnEntity.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        } catch (IOException e) {
            log.error(e.getMessage());
            yarnEntity.setIsTrash(true);
        }

        return yarnEntity;
    }

    // 封装queue metric
    private List<QueueMetricEntity> reportQueueMetricEntitys(){
        List<QueueMetricEntity> queueMetricEntities = new ArrayList<>();
        String nowTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

        List<String> resourceManagerUris = Arrays.asList(resourceManagerUri.split(","));
        if(resourceManagerUris.isEmpty()){
            return queueMetricEntities;
        }

        try {
            String queueMetricUrl = String.format(JMXSERVERURLFORMAT, getActiveResourceManagerUri(resourceManagerUris), QUEUEMETRICSALL);
            HadoopMetric queueMetrics = statefulHttpClient.get(HadoopMetric.class, queueMetricUrl, null, null);
            List<Map<String, Object>> beans = queueMetrics.getBeans();
            if(beans != null){
                for(Map<String, Object> bean: beans){
                    QueueMetricEntity queueMetricEntity = new QueueMetricEntity();
                    queueMetricEntity.setPendingApps((int) bean.get("AppsPending"));
                    queueMetricEntity.setRunningApps((int) bean.get("AppsRunning"));
                    queueMetricEntity.setActiveUsers((int) bean.get("ActiveUsers"));
                    queueMetricEntity.setAllocatedContainers((int) bean.get("AllocatedContainers"));
                    queueMetricEntity.setAllocatedMemory(Long.parseLong(bean.get("AllocatedMB").toString()));
                    queueMetricEntity.setAvailableMemory(Long.parseLong(bean.get("AvailableMB").toString()));
                    queueMetricEntity.setReservedMemory(Long.parseLong(bean.get("ReservedMB").toString()));
                    queueMetricEntity.setPendingContainers((int) bean.get("PendingContainers"));
                    queueMetricEntity.setPendingMemory(Long.parseLong(bean.get("PendingMB").toString()));
                    queueMetricEntity.setQueueName((String) bean.get("tag.Queue"));
                    queueMetricEntity.setMetricTime(nowTime);
                    queueMetricEntity.setCreateTime(nowTime);
                    queueMetricEntities.add(queueMetricEntity);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return queueMetricEntities;
    }

}
