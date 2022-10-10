package az.uni.unitechapp.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final Key jwtSecretKey;

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String issueToken(Long userId, String username) {
        return Jwts.builder()
                .setId(userId.toString())
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

}
