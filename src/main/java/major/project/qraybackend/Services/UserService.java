package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import major.project.qraybackend.Entity.LoginResponse;
import major.project.qraybackend.Entity.UserBasicData;
import major.project.qraybackend.Entity.UserDocumentReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public ApiFuture<DocumentReference> addUserDocuments(UserDocumentReference userDocumentReference, String userUid) {
        return getUserCollection().document(userUid).collection("Documents").add(userDocumentReference);
    }

    public List<QueryDocumentSnapshot> getAllDocuments(String userId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getUserCollection().document(userId).collection("Documents").get();
        return future.get().getDocuments();
    }

    public ApiFuture<WriteResult> deleteUserDocument(String documentId, String userId) {
        return getUserCollection().document(userId).collection("Documents").document(documentId).delete();
    }
}

