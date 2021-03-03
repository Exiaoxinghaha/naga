package hipeer.naga.entity.system;

import hipeer.naga.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "naga_user")
@Proxy(lazy = false)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userUnid;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userTeam;
    private String userEmail;
    private String userStatus;

    @Override
    public String toString(){
        return String.format("User: %s", userName);
    }
}
