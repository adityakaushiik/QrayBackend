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
import java.util.HashMap;
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
            File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()),
                    Objects.requireNonNull(file.getContentType())
                            .replace("application/", ".")
                            .replace("image/", "."));
            file.transferTo(tempFile);

            String fileId = UUID.randomUUID().toString();
            Map<String, String> fileMetadata = new HashMap<>();
            fileMetadata.put("originalFilename", file.getOriginalFilename());
            fileMetadata.put("fileId", fileId);
            fileMetadata.put("contentType", file.getContentType());

            String newFileName = "Documents" + "/" + fileId;

            BlobId blobId = BlobId.of("q-ray-78930.appspot.com", newFileName);
            fileMetadata.put("blobId", blobId.toString());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .setMetadata(fileMetadata).build();
            storage.create(blobInfo, Files.readAllBytes(tempFile.toPath()));

            tempFile.delete();
            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }

    public URL downloadDocument(String documentReference) {
        return storage.signUrl(BlobInfo.newBuilder("q-ray-78930.appspot.com",
                documentReference).build(), 2, TimeUnit.DAYS);
    }

    public String deleteDocument(String documentReference) {
        boolean delete = storage.delete("q-ray-78930.appspot.com", documentReference);
        System.out.println(delete);
        if (!delete)
            return "Not Found";
        return "Deleted";
    }

    public String updateDocument(String documentReference, MultipartFile file) {
        System.out.println(documentReference);
        try {
            Map<String, String> metadata = storage.get("q-ray-78930.appspot.com", documentReference).getMetadata();
            Map<String, String> newMetadata = new HashMap<>();

            File tempFile = File.createTempFile(Objects.requireNonNull(file.getOriginalFilename()),
                    Objects.requireNonNull(file.getContentType())
                            .replace("application/", ".")
                            .replace("image/", "."));
            file.transferTo(tempFile);

            if (storage.delete("q-ray-78930.appspot.com", documentReference)) {

                newMetadata.put("isReplaced", "true");
                newMetadata.put("replacedFileOriginalName", metadata.get("originalFilename"));
                newMetadata.put("replacedFileContentType", metadata.get("contentType"));
                newMetadata.put("originalFilename", file.getOriginalFilename());
                newMetadata.put("contentType", file.getContentType());

                BlobId blobId = BlobId.of("q-ray-78930.appspot.com", documentReference);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType(file.getContentType())
                        .setMetadata(newMetadata).build();
                storage.create(blobInfo, Files.readAllBytes(tempFile.toPath()));
                tempFile.delete();

                return documentReference;
            } else
                return "Not Found";

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }
}
