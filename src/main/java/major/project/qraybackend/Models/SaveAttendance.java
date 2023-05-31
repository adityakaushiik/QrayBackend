package major.project.qraybackend.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SaveAttendance {
    private String attendersId;
    private String displayName;
    private String email;
    private String addedDateTime;
}
