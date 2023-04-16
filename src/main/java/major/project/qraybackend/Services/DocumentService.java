package major.project.qraybackend.Services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class DocumentService {
    @Autowired
    Storage storage;

    public String uploadDocument(MultipartFile file) {
        try {
            File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()),
                    Objects.requireNonNull(file.getContentType()).replace("application/", "."));
            file.transferTo(tempFile);


            Map<String, String> fileMetadata = Map.of("fileOriginalName", file.getOriginalFilename());
            String folderName = "Documents";
            String newFileName = folderName + "/" + UUID.randomUUID() + file.getContentType().replace("application/", ".");


            BlobId blobId = BlobId.of("q-ray-78930.appspot.com", newFileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").setMetadata(fileMetadata).build();
            storage.create(blobInfo, Files.readAllBytes(tempFile.toPath()));


            tempFile.delete();
            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }


//    public void downloadDocument() {
//
//    }
//
//    public void deleteDocument() {
//
//    }
//
//    public void updateDocument() {
//
//    }
//
//    public void getDocumentsList(String userId) {
//
//    }
}
