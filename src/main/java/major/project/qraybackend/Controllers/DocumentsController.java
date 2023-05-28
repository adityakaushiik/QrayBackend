package major.project.qraybackend.Controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.annotations.ApiParam;
import major.project.qraybackend.Entity.UserDocumentReference;
import major.project.qraybackend.Services.DocumentService;
import major.project.qraybackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/documents/")
public class DocumentsController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private FirebaseAuthenticationService firebaseAuthService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> uploadDocument(@RequestPart("uid") String uid,
                                                 @RequestPart("document") @ApiParam(value = "File", required = true) MultipartFile document,
                                                 @RequestPart("documentType") String documentType) throws FirebaseAuthException {
//        if (response.getStatus() != 200) {
//            return ResponseEntity.ok(response);
//        }
//        HttpServletRequest request,
//        String accessToken = request.getHeader("Authorization");
//        FirebaseToken firebaseToken = firebaseAuthService.verifyAuthToken(accessToken);

//        String uid = firebaseToken.getUid();
//        String uid = (String) request.getAttribute("uid");
//        System.out.println(uid);


        UserDocumentReference userDocumentReference = new UserDocumentReference(documentType, documentService.uploadDocument(document));
        ApiFuture<DocumentReference> writeResultApiFuture = userService.addUserDocuments(userDocumentReference, uid);
        System.out.println(writeResultApiFuture.isDone());
        return ResponseEntity.ok(userDocumentReference);
    }

    @GetMapping(value = "/getDocuments/{userId}")
    public ResponseEntity<Map<String, Object>> getDocumentListing(@PathVariable String userId) throws ExecutionException, InterruptedException {
        System.out.println(userId);
        List<QueryDocumentSnapshot> allDocuments = userService.getAllDocuments(userId);
        Map<String, Object> documentMap = new HashMap<>();
        for (QueryDocumentSnapshot document : allDocuments) {
            documentMap.put(document.getId(), document.getData());
        }
        return ResponseEntity.ok(documentMap);
    }

    @PostMapping(value = "/download/")
    public ResponseEntity<URL> getDocument(@RequestParam("documentReference") String documentReference) {
        System.out.println("downloading " + documentReference);
        return ResponseEntity.ok(documentService.downloadDocument(documentReference));
    }

    @PostMapping(value = "/delete/")
    public ResponseEntity<String> deleteDocument(@RequestParam("documentId") String documentId,
                                                 @RequestParam("userId") String userId,
                                                 @RequestParam("documentReference") String documentReference) throws ExecutionException, InterruptedException {
        System.out.println("Deleting " + documentReference);
        if (documentService.deleteDocument(documentReference).equals("Deleted"))
            return ResponseEntity.ok((userService.deleteUserDocument(documentId, userId).toString()));
        return ResponseEntity.ok("Error , Document not deleted");
    }

    @PutMapping(value = "/update/")
    public ResponseEntity<String> updateDocument(@RequestPart("documentReference") String documentReference,
                                                 @RequestPart("document")
                                                 @ApiParam(value = "File", required = true) MultipartFile document) {
        System.out.println("Updating " + documentReference);
        documentService.updateDocument(documentReference, document);
        return ResponseEntity.ok("Updated");

    }
}


//get -> query
//post -> body

//put
//delete










