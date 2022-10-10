package az.uni.unitechapp.filter;

import az.uni.unitechapp.constant.HttpHeaderConstant;
import az.uni.unitechapp.jwt.JwtService;
import az.uni.unitechapp.model.CustomAuthenticationToken;
import az.uni.unitechapp.model.UserClaims;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AuthRequestFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        getAuthentication(request).ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
        filterChain.doFilter(request, response);
    }

    public Optional<Authentication> getAuthentication(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(HttpHeaderConstant.AUTHORIZATION))
                .filter(this::isBearerAuth)
                .flatMap(this::getAuthenticationBearer);
    }

    private boolean isBearerAuth(String header) {
        return header.startsWith(TOKEN_PREFIX);
    }

    private Optional<Authentication> getAuthenticationBearer(String header) {
        String token = header.substring(TOKEN_PREFIX.length()).trim();
        Claims claims = jwtService.parseToken(token);
        log.info("The claims parsed {}", claims);
        return Optional.of(getAuthenticationBearer(claims));
    }

    private Authentication getAuthenticationBearer(Claims claims) {
        UserClaims userClaims = UserClaims.builder()
                .id(Long.valueOf(claims.getId()))
                .pin(claims.getSubject())
                .build();
        return new CustomAuthenticationToken(userClaims);
    }

}
