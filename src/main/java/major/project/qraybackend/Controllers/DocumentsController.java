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

import java.util.List;
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
    public ResponseEntity<List<QueryDocumentSnapshot>> getAllDocuments(@PathVariable String userId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(userService.getAllDocuments(userId));
    }
}
