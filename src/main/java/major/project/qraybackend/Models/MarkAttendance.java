package major.project.qraybackend.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkAttendance {
    private String attendanceId;
    private String attendersId;
    private String displayName;
    private String email;
}
