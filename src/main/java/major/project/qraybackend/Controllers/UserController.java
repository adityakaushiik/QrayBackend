package major.project.qraybackend.Controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import major.project.qraybackend.Entity.LoginResponse;
import major.project.qraybackend.Entity.UserBasicData;
import major.project.qraybackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("user/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<Object> userLogin(@RequestParam("email") String email,
                            @RequestParam("password") String password) throws FirebaseAuthException {
        LoginResponse loginResponse = userService.userLogin(email, password);
        if (loginResponse == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> userRegister(@RequestBody UserBasicData userBasicData)
            throws FirebaseAuthException, ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResultApiFuture = userService.addUserData(userBasicData);
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put("message", "User registered successfully");
        responseBody.put("user", userBasicData);
        responseBody.put("time", writeResultApiFuture.get().getUpdateTime().toDate());
        responseBody.put("status", HttpStatus.OK);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
