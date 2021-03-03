package hipeer.naga.server.jwt;

import hipeer.naga.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object){
        String token = request.getHeader("token");
        Long userId = Long.parseLong(JwtManager.parseJwt(token).getId());

        ContextUtil.setCurrentUser(userService.finUserById(userId));

        return true;
    }
}
