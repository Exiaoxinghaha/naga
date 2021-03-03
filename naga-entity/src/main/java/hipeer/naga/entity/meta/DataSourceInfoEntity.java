package hipeer.naga.entity.meta;

import hipeer.naga.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 数据源信息
 */
@Entity
@Data
@Table(name = "naga_datasource_info")
public class DataSourceInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 数据源名称
    private String dataSourceName;

    // 数据源类型
    @Enumerated(value = EnumType.STRING)
    private SourceType dataSourceType;

    // 数据源连接信息
    @Column(columnDefinition = "longtext")
    private String dataSourceConnectInfo;

    // 所属业务线名称
    private String projectName;

    // 所属的业务线id
    private Long projectId;

    // 管理员
    private String admin;

    // team
    private String team;


    @Override
    public String toString(){
        return String.format("DataSource %s", dataSourceName);
    }
}
