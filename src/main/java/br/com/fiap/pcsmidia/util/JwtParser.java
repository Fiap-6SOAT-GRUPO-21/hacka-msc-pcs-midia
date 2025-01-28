package br.com.fiap.pcsmidia.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Slf4j
public class JwtParser {

    public static String getPhoneNumber() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || token.isBlank()) throw new RuntimeException("Token não informado");
        if (token.contains("Bearer ")) token = token.substring(7);
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();

        if (claims.get("username") == null) {
            log.info("ERROR ---> username not found!!!");
            return null;
        }

        return claims.get("username").asString();
    }
}
