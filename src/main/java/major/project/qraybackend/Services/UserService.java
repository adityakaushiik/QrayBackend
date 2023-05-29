package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import major.project.qraybackend.Models.LoginRequest;
import major.project.qraybackend.Models.LoginResponse;
import major.project.qraybackend.Models.UserBasicData;
import major.project.qraybackend.Models.UserDocumentReference;
import major.project.qraybackend.Utils.TokenAndPasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    private FirebaseAuth firebaseAuth;
    @Autowired
    private Firestore firestore;
    @Autowired
    private TokenAndPasswordUtil tokenAndPasswordUtil;

    private CollectionReference getUserCollection() {
        return firestore.collection("users");
    }

    public UserBasicData getUserData(String uid) {
        DocumentReference docRef = getUserCollection().document(uid);
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = docRef.get();

        try {
            return documentSnapshotApiFuture.get().toObject(UserBasicData.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    //login
    public ResponseEntity<Object> userLogin(LoginRequest request) throws FirebaseAuthException {
        UserRecord userDetails = firebaseAuth.getUserByEmail(request.getEmail());
        UserBasicData userBasicData = getUserData(userDetails.getUid());

        if (tokenAndPasswordUtil.verifyPassword(request.getPassword(), userBasicData.getPassword())) {

            final String token = tokenAndPasswordUtil.generateToken(userDetails.getUid());

            LoginResponse loginResponse = new LoginResponse(
                    userDetails.getUid(),
                    userDetails.getEmail(),
                    userDetails.getDisplayName(),
                    token
            );

            return ResponseEntity.ok(loginResponse);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    //register
    public ApiFuture<WriteResult> addUserData(UserBasicData userData) throws FirebaseAuthException {

        userData.setPassword(tokenAndPasswordUtil.encodePassword(userData.getPassword()));

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(userData.getEmail())
                .setEmailVerified(false)
                .setPassword(userData.getPassword())
                .setDisplayName(userData.getFirstName() + " " + userData.getLastName())
                .setDisabled(false);

        UserRecord userRecord = firebaseAuth.createUser(request);
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

//        try {
//            UserRecord userByEmail = FirebaseAuth.getInstance().getUserByEmail(request.getEmail());
//            String authToken = firebaseAuthService.generateAuthToken(request.getEmail(), request.getPassword());
//
//
//            return ResponseEntity.ok(new LoginResponse(userByEmail.getUid(),
//                    userByEmail.getEmail(),
//                    userByEmail.getDisplayName(),
//                    authToken));
//
//        } catch (FirebaseAuthException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }