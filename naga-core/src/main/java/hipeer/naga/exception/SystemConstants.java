package hipeer.naga.exception;

public interface SystemConstants {

    // 正常返回
    public static final int SYSTEM_SUCESS = 200;

    // 系统异常
    public static final int SYSTEM_EXCEPTION = 201;

    // 参数异常
    public static final int ERROR_INPUT_PARAM = 202;

    // 用户不存在
    public static final int ERROR_USER_NOT_EXISTS = 203;

    // 密码错误
    public static final int ERROR_USER_PASSWORD = 204;

    // 权限不足
    public static final int ERROR_PERMISSION = 205;


    public static final String JWT_SECRET_KEY = "admin123";
}
