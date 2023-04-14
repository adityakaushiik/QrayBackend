package major.project.qraybackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserBankDetails {
    String accountNumber;
    String ifscCode;
    String bankName;
    String branchName;
    String accountHolderName;
}
