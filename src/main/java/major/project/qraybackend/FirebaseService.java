package major.project.qraybackend;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@Service
public class FirebaseService{
    private final FileInputStream refreshToken = new FileInputStream("C:\\Users\\User\\Downloads\\q-ray-firebase-adminsdk.json");

    public FirebaseService() throws FileNotFoundException {
    }

    public String addData(Map<String, Object> docData , String collectionName , String documentId) throws Exception {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> future = db.collection(collectionName).document(documentId).set(docData);
        System.out.println("Update time : " + future.get().getUpdateTime());
        return "Data added successfully";
    }
}


