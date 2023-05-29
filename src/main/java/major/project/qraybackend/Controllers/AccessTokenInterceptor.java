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

        // Validate the access token
        boolean isValid = validateToken(accessToken);

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

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // This method is called after the handler is executed, but before the view is rendered
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        // This method is called after the complete request has finished and the view has been rendered
//    }

//    private String extractAccessToken(HttpServletRequest request) {
//        // Implement the logic to extract the access token from the request
//        // For example, you can extract it from the Authorization header or query parameters
//        // Return the extracted access token
//    }

//    private boolean validateAccessToken(String accessToken) {
//        // Implement the logic to validate the access token
//        // Perform the necessary checks, such as signature verification, expiration, and database lookup
//        // Return true if the token is valid, false otherwise
//    }
