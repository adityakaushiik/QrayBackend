package major.project.qraybackend.Config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirestoreConfig {
    private final Credentials credentials = GoogleCredentials.fromStream(
            new FileInputStream("src/main/resources/q-ray-firebase-adminsdk.json"));

    public FirestoreConfig() throws IOException {
    }

    @Bean
    public Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    @Bean
    public Storage getStorage() {
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    @Bean
    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

}
