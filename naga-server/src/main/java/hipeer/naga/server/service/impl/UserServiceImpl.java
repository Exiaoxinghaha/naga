package hipeer.naga.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import hipeer.naga.entity.system.UserEntity;
import hipeer.naga.repository.system.UserRepository;
import hipeer.naga.server.service.UserService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;

    // 初始化系统用户
    @PostConstruct
    public void init(){

        UserEntity admin = userRepository.findOneByUserName("admin");
        if(null == admin){
            UserEntity userEntity = UserEntity.builder()
                    .userName("admin")
                    .userPassword(SecureUtil.md5("123456"))
                    .userTeam("")
                    .userPhone("13800000000")
                    .userStatus("Y")
                    .userEmail("admin@163.com")
                    .build();
            userEntity.setIsTrash(false);
            userEntity.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            userRepository.save(userEntity);
        }
    }

    @Override
    public Page<UserEntity> UserList(int page, int size, String sort, Sort.Direction direction) {
        return userRepository.findAll(Example.of(UserEntity.builder().build()), PageRequest.of(page, size,
                Sort.by(direction == null ? Direction.DESC : direction, ObjectUtil.isNull(sort) ? "userUnid": sort)));
    }

    @Override
    public void addUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(long id) {
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setIsTrash(true);
        userRepository.save(userEntity);
    }


    @Override
    public UserEntity findUserByName(String username) {
        return userRepository.findOneByUserName(username);
    }

    @Override
    public UserEntity finUserById(Long id) {
        return userRepository.findById(id).get();
    }


}
