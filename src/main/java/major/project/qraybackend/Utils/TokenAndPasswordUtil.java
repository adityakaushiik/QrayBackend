package major.project.qraybackend.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class TokenAndPasswordUtil {
    private final String secretKey = "yourSecretKey"; // Replace with your own secret key
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateToken(String username) {
        Key key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000 * 4)) // Token expires in 4 days
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean verifyPassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
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

    public String generateToken(String qrLinkId, String userID) {
        Key key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .claim("userID", userID) // Include the email as a claim
                .setSubject(qrLinkId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000 * 4)) // Token expires in 4 days
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getClaimFromToken(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userID", String.class);
    }
}
//
