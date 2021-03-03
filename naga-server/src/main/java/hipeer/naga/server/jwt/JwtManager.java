package hipeer.naga.server.jwt;

import hipeer.naga.entity.system.UserEntity;
import hipeer.naga.exception.SystemConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtManager {

    // 生成jwt token
    public static String createJwt(long millis, UserEntity userEntity){
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userUnid", userEntity.getUserUnid());
        claims.put("userName", userEntity.getUserName());
        claims.put("userPassword", userEntity.getUserPassword());

        String subObject = userEntity.getUserName();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(subObject)
                .setSubject(subObject)
                .signWith(SignatureAlgorithm.HS256, SystemConstants.JWT_SECRET_KEY);
        if(millis >= 0){
            long expMillis = nowMillis + millis;
            Date exp = new Date(expMillis);
            jwtBuilder.setExpiration(exp);
        }

        return jwtBuilder.compact();

    }

    // 解析jwt token
    public static Claims parseJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SystemConstants.JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    // 验证 jwt token
    public static Boolean isVerify(String token, String userPassword){
        Claims claims = parseJwt(token);
        return claims.get("userPassword").equals(userPassword);
    }
}
