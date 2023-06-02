package major.project.qraybackend.Controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import major.project.qraybackend.Models.LoginRequest;
import major.project.qraybackend.Models.UserBasicData;
import major.project.qraybackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> userLogin(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getEmail() + " " + loginRequest.getPassword());
        return userService.userLogin(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> userRegister(@RequestBody UserBasicData userBasicData)
            throws FirebaseAuthException, ExecutionException, InterruptedException {
        System.out.println(userBasicData.getEmail() + " " + userBasicData.getPassword());

        ApiFuture<WriteResult> writeResultApiFuture = userService.addUserData(userBasicData);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "User registered successfully");
        responseBody.put("user", userBasicData);
        responseBody.put("time", writeResultApiFuture.get().getUpdateTime().toDate());
        responseBody.put("status", HttpStatus.OK);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/userDetails")
    public ResponseEntity<Object> getUserData(HttpServletRequest request) {
        return new ResponseEntity<>(userService.userBasicDataWithoutPassword(request.getAttribute("uid").toString()), HttpStatus.OK);
    }

    @PutMapping("/updateUserDetails")
    public ResponseEntity<Object> updateUserDetails(HttpServletRequest request, @RequestBody Map<String, Object> updateData) {
        return new ResponseEntity<>(userService.updateUserBasicData(updateData, request.getAttribute("uid").toString()), HttpStatus.OK);
    }
}
