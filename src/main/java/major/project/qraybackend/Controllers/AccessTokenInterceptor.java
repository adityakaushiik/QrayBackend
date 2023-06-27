package major.project.qraybackend.Controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessTokenInterceptor implements HandlerInterceptor {
    private final String secretKey = "yourSecretKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Extract the access token from the request (e.g., from headers or query parameters)
        String accessToken = request.getHeader("Authorization");

        if (accessToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing access token");
            return false;
        }

        boolean isValid = validateToken(accessToken);

        // Validate the access token
        if (isValid) {
            request.setAttribute("uid", getUsernameFromToken(accessToken));
            return true;
        } else {
            // Token is invalid, return an appropriate response or redirect
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        token = token.replace("Bearer ", "");
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}