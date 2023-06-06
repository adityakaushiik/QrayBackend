package major.project.qraybackend.Services;

//@Service
//public class UserService {
//    @Autowired
//    private FirebaseAuth firebaseAuth;
//    @Autowired
//    private Firestore firestore;
//    @Autowired
//    private TokenAndPasswordUtil tokenAndPasswordUtil;
//
//    private CollectionReference getUserCollection() {
//        return firestore.collection("users");
//    }
//
//    private Map<String, Object> getUserDataMap(String uid) {
//        DocumentReference docRef = getUserCollection().document(uid);
//        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = docRef.get();
//
//        try {
//            return documentSnapshotApiFuture.get().getData();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private UserBasicData getUserData(String uid) {
//        DocumentReference docRef = getUserCollection().document(uid);
//        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = docRef.get();
//
//        try {
//            return documentSnapshotApiFuture.get().toObject(UserBasicData.class);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    //login
//    public ResponseEntity<Object> userLogin(LoginRequest request) {
//        UserRecord userDetails;
//        try {
//            userDetails = firebaseAuth.getUserByEmail(request.getEmail());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//
//        UserBasicData userBasicData = getUserData(userDetails.getUid());
//
//        if (tokenAndPasswordUtil.verifyPassword(request.getPassword(), userBasicData.getPassword())) {
//            final String token = tokenAndPasswordUtil.generateToken(userDetails.getUid());
//
//            LoginResponse loginResponse = new LoginResponse(
//                    userDetails.getUid(),
//                    userDetails.getEmail(),
//                    userDetails.getDisplayName(),
//                    token
//            );
//
//            return ResponseEntity.ok(loginResponse);
//        } else
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }
//
//    //register
//    public ApiFuture<WriteResult> addUserData(UserBasicData userData) throws FirebaseAuthException {
//
//        userData.setPassword(tokenAndPasswordUtil.encodePassword(userData.getPassword()));
//
//        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
//                .setEmail(userData.getEmail())
//                .setEmailVerified(false)
//                .setPassword(userData.getPassword())
//                .setDisplayName(userData.getFirstName() + " " + userData.getLastName())
//                .setDisabled(false);
//
//        UserRecord userRecord = firebaseAuth.createUser(request);
//        return getUserCollection().document(userRecord.getUid()).set(userData);
//    }
//
//    public Object userBasicDataWithoutPassword(String uid) {
//        Map<String, Object> userDataMap = getUserDataMap(uid);
//        userDataMap.remove("password");
//        return userDataMap;
//    }
//
//    public boolean updateUserBasicData(Map<String, Object> updateData, String uid) {
//        ApiFuture<WriteResult> update = getUserCollection().document(uid).update(updateData);
//        return update.isDone();
//    }
//
//
//    ///////////////////////////////for documents
//    public ApiFuture<DocumentReference> addUserDocuments(UserDocumentReference userDocumentReference, String userUid) {
//        return getUserCollection().document(userUid).collection("Documents").add(userDocumentReference);
//    }
//
//    public List<Object> getAllDocuments(String userId) throws ExecutionException, InterruptedException {
//        List<Object> list = new ArrayList<>();
//        getUserCollection().document(userId).collection("Documents").get().get().getDocuments().forEach(
//                documentSnapshot -> {
//                    var data = documentSnapshot.getData();
//                    data.put("documentId", documentSnapshot.getId());
//                    list.add(data);
//                }
//        );
//
//        return list;
//    }
//
//    public ApiFuture<WriteResult> deleteUserDocument(String documentId, String userId) {
//        return getUserCollection().document(userId).collection("Documents").document(documentId).delete();
//    }
//}\

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private Map<String, Object> getUserDataMap(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getUserCollection().document(uid);
        DocumentSnapshot documentSnapshot = docRef.get().get();
        return documentSnapshot.getData();
    }

    private UserBasicData getUserData(String uid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getUserCollection().document(uid);
        DocumentSnapshot documentSnapshot = docRef.get().get();
        return documentSnapshot.toObject(UserBasicData.class);
    }

    //login
    public ResponseEntity<Object> userLogin(LoginRequest request) {
        try {
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
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (FirebaseAuthException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
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

    public Object userBasicDataWithoutPassword(String uid) {
        try {
            Map<String, Object> userDataMap = getUserDataMap(uid);
            userDataMap.remove("password");
            return userDataMap;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUserBasicData(Map<String, Object> updateData, String uid) {
        ApiFuture<WriteResult> update = getUserCollection().document(uid).update(updateData);
        return update.isDone();
    }

    ///////////////////////////////for documents
    public ApiFuture<DocumentReference> addUserDocuments(UserDocumentReference userDocumentReference, String userUid) {
        return getUserCollection().document(userUid).collection("Documents").add(userDocumentReference);
    }

    public List<Object> getAllDocuments(String userId) throws ExecutionException, InterruptedException {
        List<Object> list = new ArrayList<>();
        QuerySnapshot querySnapshot = getUserCollection().document(userId).collection("Documents").get().get();
        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
            var data = documentSnapshot.getData();
            data.put("documentId", documentSnapshot.getId());
            list.add(data);
        }
        return list;
    }

    public ApiFuture<WriteResult> deleteUserDocument(String documentId, String userId) {
        return getUserCollection().document(userId).collection("Documents").document(documentId).delete();
    }
}
