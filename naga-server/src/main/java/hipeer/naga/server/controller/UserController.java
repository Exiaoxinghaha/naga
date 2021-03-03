package hipeer.naga.server.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.common.base.Strings;
import hipeer.naga.entity.system.UserEntity;
import hipeer.naga.exception.SystemConstants;
import hipeer.naga.server.BaseController;
import hipeer.naga.server.jwt.JwtManager;
import hipeer.naga.server.jwt.LoginRequired;
import hipeer.naga.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/naga/v1/user")
@CrossOrigin
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    // 用户登录
    @PostMapping(value = "login")
    public Object login(@RequestParam String userName, @RequestParam String userPassword){

        UserEntity userEntity = userService.findUserByName(userName);
        if(null == userEntity){
            return getErrorResult(SystemConstants.ERROR_USER_NOT_EXISTS, "用户不存在");
        }
        if(userEntity.getUserPassword().equals(SecureUtil.md5(userPassword))){

            Map<String, Object> token = new HashMap<>();
            String jwt = JwtManager.createJwt(60*60*1000L, userEntity);
            token.put("token", jwt);

            return getSucessResult(SystemConstants.SYSTEM_SUCESS, token);
        } else {
            return getErrorResult(SystemConstants.ERROR_USER_PASSWORD, "用户密码错误");
        }
    }

    // 添加用户
    @PostMapping(value = "addUser")
    @LoginRequired
    public Object addUser(@RequestBody UserEntity userEntity){
        userEntity.setUserPassword(SecureUtil.md5(userEntity.getUserPassword()));
        userEntity.setIsTrash(false);
        userEntity.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-mm HH:mm:ss"));
        userService.addUser(userEntity);
        return getSucessResult(SystemConstants.SYSTEM_SUCESS, "添加成功");
    }


    // 更新用户
    @PostMapping(value = "updateUser")
    @LoginRequired
    public Object updateUser(@RequestBody UserEntity userEntity) {
        String userPassword = userEntity.getUserPassword();
        if(!Strings.isNullOrEmpty(userPassword)){
            userEntity.setUserPassword(SecureUtil.md5(userPassword));
        }
        userService.updateUser(userEntity);

        return getSucessResult(SystemConstants.SYSTEM_SUCESS, "更新成功");
    }


    // 删除用户
    @PostMapping(value = "deleteUser")
    @LoginRequired
    public Object deleteUser(@RequestParam long id){
        userService.deleteUser(id);
        return getSucessResult(SystemConstants.SYSTEM_SUCESS, "删除成功");
    }


    @GetMapping("getAllUsers")
    public Object getAllUsers(@RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "size", defaultValue = "50") int size){
        Page<UserEntity> userEntities = userService.UserList(page - 1, size, null, null);
        Map<String, Object> pages = new HashMap<>();
        pages.put("pages", userEntities.getContent());
        pages.put("pageIndex", page);
        pages.put("pageSize", size);
        pages.put("pageCount", userEntities.getTotalPages());

        return getSucessResult(SystemConstants.SYSTEM_SUCESS, pages);
    }

}
