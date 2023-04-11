package major.project.qraybackend;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService{

    public String addData(Map<String, Object> docData , String collectionName , String documentId)
            throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> future = db.collection(collectionName).document(documentId).set(docData);
        System.out.println("Update time : " + future.get().getUpdateTime());
        return "Data added successfully";
    }
}


