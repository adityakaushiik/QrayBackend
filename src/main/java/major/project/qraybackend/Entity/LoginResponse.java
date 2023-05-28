package major.project.qraybackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String uid;
    private String email;
    private String displayName;
    private String accessToken;
//    private String refreshToken;
}
