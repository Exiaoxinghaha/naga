package hipeer.naga.entity.meta;

import hipeer.naga.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "naga_project_info")
public class ProjectInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 业务线名称
    private String projectName;

    // 业务线的hdfs命名空间
    private String projectNameSpace;

    // 业务线描述
    private String projectDetail;

    // 业务线的hdfs目录
    private String projectHdfsPath;

    // 业务线的yarn队列
    private String projectYarnQueue;

    // 业务线空间配额
    private Long projectDataSpaceQuota;

    // 业务线文件数配额
    private Long projectFileNumsQuota;

    // 业务线管理员
    private String admin;

    // 业务线team
    private String team;

    @Override
    public String toString(){
        return String.format("Project %s", projectName);
    }
}
