package major.project.qraybackend.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrLinkAccessRequest {
    String userAgent;
    String screenWidth;
    String screenHeight;
    String deviceType;
    String token;
    String ip;
}


//one time access
//guest access