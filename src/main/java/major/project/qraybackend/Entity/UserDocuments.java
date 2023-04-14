package major.project.qraybackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDocuments {
    String aadharCard;
    String panCard;
    String drivingLicense;
    String voterId;
    String passport;
}
