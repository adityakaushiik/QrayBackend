package major.project.qraybackend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class QrayBackendApplication {

    public static void main(String[] args) throws IOException {

        FileInputStream refreshToken = new
                FileInputStream("src/main/resources/q-ray-firebase-adminsdk.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();
        FirebaseApp.initializeApp(options);


        SpringApplication.run(QrayBackendApplication.class, args);
    }
}
