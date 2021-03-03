package hipeer.naga.repository.system;

import hipeer.naga.entity.system.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findOneByUserName(String userName);
}
