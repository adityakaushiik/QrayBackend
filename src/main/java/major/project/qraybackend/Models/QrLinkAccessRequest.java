package major.project.qraybackend.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrLinkAccessRequest {
    String _deviceType;
    String _ipAddress;
    int _screenHeight;
    int _screenWidth;
    String _userAgent;
}


//one time access
//guest access