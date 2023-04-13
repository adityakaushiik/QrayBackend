package major.project.qraybackend.Entity;


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
}
