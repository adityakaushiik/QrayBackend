package major.project.qraybackend;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService{

    @PostConstruct
    public void fetchFirestoreData() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection("users").get().get().getDocuments().forEach(documentSnapshot -> {
            System.out.println(documentSnapshot.getData());
        });
    }
}


