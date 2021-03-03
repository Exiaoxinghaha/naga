package hipeer.naga.server.jwt;

import hipeer.naga.entity.system.UserEntity;

public class ContextUtil {

    private static ThreadLocal<UserEntity> local = new ThreadLocal<>();

    public static void setCurrentUser(UserEntity userEntity) {
        local.set(userEntity);
    }

    public static UserEntity getCurrentUser(){
        return local.get();
    }
}
