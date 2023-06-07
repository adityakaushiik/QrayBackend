package major.project.qraybackend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserBasicData {
    String firstName;
    String lastName;
    String state;
    String country;
    String email;
    String password;
    String phoneNumber;
//    String collegeEnrollmentNumber;
//    String fathersName;
//    String mothersName;
    //address
}
