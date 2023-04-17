package major.project.qraybackend.Services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DocumentService {
    @Autowired
    Storage storage;

    public String uploadDocument(MultipartFile file) {
        try {
            String extension = Objects.requireNonNull(file.getContentType())
                    .replace("application/", ".")
                    .replace("image/", ".");

            File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()), extension);
            file.transferTo(tempFile);

            Map<String, String> fileMetadata = Map.of("fileOriginalName", file.getOriginalFilename());
            String folderName = "Documents";
            String newFileName = folderName + "/" + UUID.randomUUID() + extension;

            uploadLogic(newFileName, extension, tempFile, fileMetadata);

            tempFile.delete();
            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }

    private String uploadLogic(String newFileName,
                               String extension,
                               File tempFile,
                               Map<String, String> fileMetadata) throws IOException {
        BlobId blobId = BlobId.of("q-ray-78930.appspot.com", newFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(extension)
                .setMetadata(fileMetadata).build();
        storage.create(blobInfo, Files.readAllBytes(tempFile.toPath()));
        return newFileName;
    }

    public URL downloadDocument(String documentReference) { //, String userId
        return storage.signUrl(BlobInfo.newBuilder("q-ray-78930.appspot.com",
                documentReference).build(), 2, TimeUnit.DAYS);
    }

    public String deleteDocument(String documentReference) {
        boolean delete = storage.delete("q-ray-78930.appspot.com", documentReference);
        if (!delete)
            return "Not Found";
        return "Deleted";
    }

    public String updateDocument(String documentReference, String documentType, MultipartFile file, String userId) {

        boolean delete = storage.delete("q-ray-78930.appspot.com", documentReference);
        return null;
    }
}
