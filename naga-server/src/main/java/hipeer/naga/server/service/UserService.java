package hipeer.naga.server.service;

import hipeer.naga.entity.system.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface UserService {

    // 用户分页列表
    Page<UserEntity> UserList(int page, int size, String sort, Sort.Direction direction);

    // 添加用户
    void addUser(UserEntity userEntity);

    // 更新用户
    void updateUser(UserEntity userEntity);

    // 删除用户
    void deleteUser(long id);

    // 根据用户名查找
    UserEntity findUserByName(String userName);


    UserEntity finUserById(Long id);
}
