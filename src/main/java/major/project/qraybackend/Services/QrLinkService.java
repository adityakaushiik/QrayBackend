package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import major.project.qraybackend.Models.QrLinkAccessRequest;
import major.project.qraybackend.Utils.TokenAndPasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class QrLinkService {
    @Autowired
    private Firestore firestore;
    @Autowired
    private TokenAndPasswordUtil tokenAndPasswordUtil;
    @Autowired
    private DocumentService documentService;

    private CollectionReference getUserCollection() {
        return firestore.collection("users");
    }

    public String createQrLink(String uid, String type, String sessionName, int time, String[] documentIds)
            throws ExecutionException, InterruptedException {


        ApiFuture<DocumentReference> qr = getUserCollection().document(uid)
                .collection("qr-link")
                .add(Map.of(
                        "creationDate", LocalDateTime.now().toString(),
                        "documentIds", Arrays.stream(documentIds).toList(),
                        "sessionType", type,
                        "sessionName", sessionName,
                        "sessionValidTime", LocalDateTime.now().plusMinutes(60L * time).toString()));

        String token = tokenAndPasswordUtil.generateToken(qr.get().getId(), uid);
        var ref = qr.get();
        ref.update("token", token);
        return token;
    }


    public ArrayList<Object> getQrLinks(String uid) throws ExecutionException, InterruptedException {
        try {
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = getUserCollection().document(uid)
                    .collection("qr-link").get();

            ArrayList<Object> list = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : querySnapshotApiFuture.get().getDocuments()) {
                Map<String, Object> data = documentSnapshot.getData();
                List<Object> documentList = retrieveDocumentList(uid, data);

                data.put("documents", documentList);
                data.put("id", documentSnapshot.getId());
                list.add(data);
            }
            return list;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Object> retrieveDocumentList(String uid, Map<String, Object> qrData) throws ExecutionException, InterruptedException {
        List<Object> documentList = new ArrayList<>();
        for (String doc : (List<String>) qrData.get("documentIds")) {
            DocumentSnapshot docSnapshot = getUserCollection().document(uid).collection("Documents").document(doc).get().get();
            if (docSnapshot.exists()) {
                Map<String, Object> docData = docSnapshot.getData();
                if (docData != null) {
                    docData.put("id", doc);
                    documentList.add(docData);
                }
            }
        }
        return documentList;
    }


    public ApiFuture<WriteResult> deleteQrLink(String uid, String qrId)
            throws ExecutionException, InterruptedException {
        return getUserCollection().document(uid).collection("qr-link").document(qrId).delete();
    }

    public ResponseEntity<Map<String, Object>> accessQrLink(String token, QrLinkAccessRequest qrLinkAccessRequest)
            throws ExecutionException, InterruptedException {
        try {
            System.out.println(qrLinkAccessRequest.getDeviceType());
            if (!tokenAndPasswordUtil.validateToken(token))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            String userId = tokenAndPasswordUtil.getClaimFromToken(token);
            String qrId = tokenAndPasswordUtil.getSubjectFromToken(token);

            DocumentReference qrDocRef = getUserCollection()
                    .document(userId)
                    .collection("qr-link")
                    .document(qrId);

            DocumentSnapshot qrDocumentSnapshot = qrDocRef.get().get();
            Map<String, Object> qrData = qrDocumentSnapshot.getData();

            if (qrData == null || qrData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "QR Link Not Found"));
            }

            if (qrData.get("sessionValidTime").toString().compareTo(LocalDateTime.now().toString()) < 0)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Session Has Expired"));

            // Set last seen
            qrDocRef.update("lastSeen", LocalDateTime.now().toString());

            // Update Device List
            List<Object> deviceData = (List<Object>) qrData.get("deviceList");
            if (deviceData != null && deviceData.contains(qrLinkAccessRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Device Already Exists"));
            } else {
                qrDocRef.update("deviceList", FieldValue.arrayUnion(qrLinkAccessRequest));
            }

            Map<String, Object> finalData = getUserCollection()
                    .document(userId)
                    .get().get().getData();

            List<Object> documentUrls = fetchDocumentUrls(qrData, userId);

            finalData.remove("password");
            finalData.put("documents", documentUrls);
            return ResponseEntity.ok(finalData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<Object> fetchDocumentUrls(Map<String, Object> qrData, String userId) throws ExecutionException, InterruptedException {
        List<Object> documentUrls = new ArrayList<>();
        List<String> documents = (List<String>) qrData.get("documentIds");
        for (String document : documents) {
            var docData = getUserCollection().document(userId).collection("Documents").document(document).get().get().getData();
            if (docData != null) {
                String documentReference = docData.get("documentReference").toString();
                String documentUrl = String.valueOf(documentService.downloadDocument(documentReference));
                docData.put("documentUrl", documentUrl);
                documentUrls.add(docData);
            }
        }
        return documentUrls;
    }
}
