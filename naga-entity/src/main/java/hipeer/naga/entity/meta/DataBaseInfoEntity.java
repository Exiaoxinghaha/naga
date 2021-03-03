package hipeer.naga.entity.meta;

import hipeer.naga.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 数据仓库中数据库信息
 */
@Entity
@Data
@Table(name = "naga_database_info")
public class DataBaseInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 数据库名称
    private String dataBaseName;

    // 数据库描述
    private String dataBaseDetail;

    // 所属层级
    private String dataBaseLevel;

    // hdfs上存储路径
    private String hdfsUrl;

    // 所属业务线名称
    private String projectName;

    // 所属业务线id
    private Long projectId;

    // 管理员
    private String admin;

    // team
    private String team;

    public String toString(){
        return String.format("Database %s", dataBaseName);
    }
}
