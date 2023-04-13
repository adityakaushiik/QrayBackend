package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import major.project.qraybackend.Entity.LoginResponse;
import major.project.qraybackend.Entity.UserBasicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    @Autowired
    private Firestore firestore;

    private CollectionReference getUserCollection() {
        return firestore.collection("users");
    }

    //login
    public LoginResponse userLogin(String email, String password) throws FirebaseAuthException {
        UserRecord userByEmail = FirebaseAuth.getInstance().getUserByEmail(email);
        //try catch block to auth the password

        FirebaseAuth.getInstance().createCustomToken(userByEmail.getUid());
        Map<String, Object> refreshTokenClaims = new HashMap<>();
        refreshTokenClaims.put("refreshToken", true);

        return new LoginResponse(userByEmail.getUid(),
                userByEmail.getEmail(),
                userByEmail.getDisplayName(),
                FirebaseAuth.getInstance().createCustomToken(userByEmail.getUid()),
                FirebaseAuth.getInstance().createCustomToken(userByEmail.getUid(), refreshTokenClaims));
    }

    //post
    public ApiFuture<WriteResult> addUserData(UserBasicData userData)
            throws FirebaseAuthException {

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(userData.getEmail())
                .setEmailVerified(false)
                .setPassword(userData.getPassword())
                .setDisplayName(userData.getFirstName() + " " + userData.getLastName())
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        return getUserCollection().document(userRecord.getUid()).set(userData);
    }

    //get
    public String getUserData(String documentId)
            throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getUserCollection().document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        return Objects.requireNonNull(future.get().getData()).toString();
    }

    //put
    public String editUserData(){
        return "Data edited successfully";
    }

    //delete
    public String deleteUserData(){
        return "Data deleted successfully";
    }
}


