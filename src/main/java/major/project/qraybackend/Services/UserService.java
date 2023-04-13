package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //post
    public String addUserData(Map<String, Object> docData)
            throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = getUserCollection().add(docData);
        System.out.println("Update time : " + future.get().toString());
        return "Data added successfully";
    }

    //get
    public String getData(String documentId)
            throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getUserCollection().document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        System.out.println("Update time : " + future);
        return Objects.requireNonNull(future.get().getData()).toString();
    }

    //put

    //delete

}


