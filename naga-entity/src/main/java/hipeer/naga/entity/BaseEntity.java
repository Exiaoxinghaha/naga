package hipeer.naga.entity;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class BaseEntity {
    @Ignore
    private Boolean isTrash = false;
    private String createTime;

}
