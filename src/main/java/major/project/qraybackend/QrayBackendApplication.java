package major.project.qraybackend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class QrayBackendApplication {
    @Autowired
    FirebaseService firebaseService ;

    public static void main(String[] args) throws IOException {
        FileInputStream refreshToken = new
                FileInputStream("src/main/resources/q-ray-firebase-adminsdk.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();
        FirebaseApp.initializeApp(options);

        SpringApplication.run(QrayBackendApplication.class, args);
    }

    @PostConstruct
    public void run() throws Exception {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", "Los Angeles");
        docData.put("state", "CA");
        docData.put("country", "USA");
        docData.put("regions", Arrays.asList("west_coast", "social"));

        System.out.println(firebaseService.addData(docData, "users", "abc1"));
    }
}
