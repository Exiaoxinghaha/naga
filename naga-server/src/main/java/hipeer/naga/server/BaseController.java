package hipeer.naga.server;

import cn.hutool.core.date.DateUtil;
import hipeer.naga.exception.SystemConstants;
import hipeer.naga.exception.NagaException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BaseController {

    @ExceptionHandler
    @ResponseBody
    public Map<String, Object> exceptionHandle(Exception ex, HttpServletResponse response){
        ex.printStackTrace();
        // 判断是否为NagaException
        if(NagaException.class.isAssignableFrom(ex.getClass())){
            NagaException ne = (NagaException) ex;
            // 返回Naga异常
            return getResultMap(ne.getErrorCode(), ne.getErrorMsg(), null);
        } else {
            // 返回其他异常
            return getResultMap(SystemConstants.SYSTEM_EXCEPTION, ex.getMessage(), null);
        }
    }

    // 异常返回
    public Map<String, Object> getErrorResult(int errorCode, Object errorMsg){
        return getResultMap(errorCode, errorMsg, null);
    }


    // 结果返回
    public Map<String, Object> getSucessResult(int sucessCode, Object sucessMsg){
        return getResultMap(sucessCode, sucessMsg, null);
    }

    // 封装返回信息
    public Map<String, Object> getResultMap(int code, Object msg, Map<String, Object> extraMap) {

        HashMap<String, Object> result = new HashMap<>();
        String currentTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

        if(code == SystemConstants.SYSTEM_SUCESS){
            result.put("state", SystemConstants.SYSTEM_SUCESS);
        } else {
            result.put("state", code);
        }
        result.put("time", currentTime);
        result.put("msg", msg);

        if(extraMap != null && !extraMap.isEmpty()){
            result.putAll(extraMap);
        }

        return result;
    }


}
