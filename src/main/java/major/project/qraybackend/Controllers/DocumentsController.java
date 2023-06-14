package major.project.qraybackend.Controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import io.swagger.annotations.ApiParam;
import major.project.qraybackend.Models.UserDocumentReference;
import major.project.qraybackend.Services.DocumentService;
import major.project.qraybackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("api/documents/")
public class DocumentsController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> uploadDocument(HttpServletRequest request,
                                                 @RequestPart("document") @ApiParam(value = "File", required = true) MultipartFile document,
                                                 @RequestPart("documentType") String documentType) {
        System.out.println(request.getAttribute("uid").toString());
        UserDocumentReference userDocumentReference = new UserDocumentReference(documentType, documentService.uploadDocument(document));
        ApiFuture<DocumentReference> writeResultApiFuture = userService.addUserDocuments(userDocumentReference, request.getAttribute("uid").toString());

        return ResponseEntity.ok(userDocumentReference);
    }

    @GetMapping(value = "/getDocuments")
    public ResponseEntity<List<Object>> getDocumentListing(HttpServletRequest request)
            throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(userService.getAllDocuments(String.valueOf(request.getAttribute("uid"))));
    }

    @PostMapping(value = "/download/")
    public ResponseEntity<URL> getDocument(@RequestBody String documentReference) {
        return ResponseEntity.ok(documentService.downloadDocument(documentReference));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteDocument(HttpServletRequest request,
                                                 @RequestParam("documentId") String documentId,
                                                 @RequestParam("documentReference") String documentReference) {
        if (documentService.deleteDocument(documentReference).equals("Deleted")) {
            ApiFuture<WriteResult> writeResultApiFuture = userService.deleteUserDocument(documentId, request.getAttribute("uid").toString());
            return ResponseEntity.ok("Document deleted");
        }
        return ResponseEntity.ok("Error , Document not deleted");
    }

    @PostMapping(value = "/update/")
    public ResponseEntity<String> updateDocument(@RequestPart("documentReference") String documentReference, @RequestPart("document") @ApiParam(value = "File", required = true) MultipartFile document) {
        documentService.updateDocument(documentReference, document);
        return ResponseEntity.ok("Document Updated");
    }
}








