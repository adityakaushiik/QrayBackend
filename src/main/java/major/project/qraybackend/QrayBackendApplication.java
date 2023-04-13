package major.project.qraybackend;

import major.project.qraybackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class QrayBackendApplication {
    @Autowired
    UserService firebaseService ;

    public static void main(String[] args){
        SpringApplication.run(QrayBackendApplication.class, args);
    }

    @PostConstruct
    public void run() throws Exception {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", "Los Angeles");
        docData.put("state", "CA");
        docData.put("country", "USA");
        docData.put("regions", Arrays.asList("west_coast", "social"));

        System.out.println(firebaseService.addUserData(docData));

        System.out.println(firebaseService.getData("abc1"));
    }
}
