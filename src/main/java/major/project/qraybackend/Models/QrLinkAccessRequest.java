package major.project.qraybackend.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrLinkAccessRequest {
    String deviceType;
    String ipAddress;
    int screenHeight;
    int screenWidth;
    String userAgent;
}


//one time access
//guest access