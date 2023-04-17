package major.project.qraybackend.Controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
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

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<UserDocumentReference> uploadDocument(@RequestPart("document") @ApiParam(value = "File", required = true) MultipartFile document,
                                                                @RequestPart("documentType") String documentType,
                                                                @RequestPart("userId") String userUid) {
        UserDocumentReference userDocumentReference = new UserDocumentReference(documentType, documentService.uploadDocument(document));
        ApiFuture<WriteResult> writeResultApiFuture = userService.addUserDocuments(userDocumentReference, userUid);
        System.out.println(writeResultApiFuture.isDone());
        return ResponseEntity.ok(userDocumentReference);
    }

    @GetMapping(value = "/getDocuments/{userId}")
    public ResponseEntity<Map<String, String>> getDocumentListing(@PathVariable String userId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> allDocuments = userService.getAllDocuments(userId);
        Map<String, String> documentMap = new HashMap<>();
        for (QueryDocumentSnapshot document : allDocuments) {
            documentMap.put((String) document.get("documentType"), (String) document.get("documentReference"));
        }
        return ResponseEntity.ok(documentMap);
    }

    @PostMapping(value = "/download/")
    public ResponseEntity<URL> getDocument(@RequestParam("documentReference") String documentReference) {
        System.out.println("downloading " + documentReference);
        return ResponseEntity.ok(documentService.downloadDocument(documentReference));
    }

    @PostMapping(value = "/delete/")
    public ResponseEntity<String> deleteDocument(@RequestParam("documentReference") String documentReference) {
        System.out.println("Deleting " + documentReference);
        if (documentService.deleteDocument(documentReference).equals("Deleted")) {
            //logic to delete record of the data from the database
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.ok("Error , Document not deleted");
    }

    @PutMapping(value = "/update/")
    public ResponseEntity<String> updateDocument(@RequestPart("documentReference") String documentReference,
                                                 @RequestPart("documentType") String documentType,
                                                 @RequestPart("document") @ApiParam(value = "File", required = true) MultipartFile document,
                                                 @RequestPart("userId") String userId) {
        System.out.println("Updating " + documentReference);
        if (documentService.updateDocument(documentReference, documentType, document, userId).equals("Updated")) {
            //logic to Update record of the data from the database
            return ResponseEntity.ok("Updated");
        }
        return ResponseEntity.ok("Error , Document not updated");
    }
}
