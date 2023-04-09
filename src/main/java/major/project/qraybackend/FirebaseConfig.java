//package major.project.qraybackend;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//@Configuration
//public class FirebaseConfig {
//    private final FileInputStream refreshToken = new FileInputStream("path/to/refreshToken.json");
//
//    public FirebaseConfig() throws FileNotFoundException {
//    }
//
//    @Bean
//    public FirebaseApp initializeFirebaseApp() throws IOException {
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(refreshToken))
//                         .build();
//        return FirebaseApp.initializeApp(options);
//    }
//}
//
