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

    public String createQrLink(String uid, String type, String sessionName, int time, String[] documentIds) throws ExecutionException, InterruptedException {

        ApiFuture<DocumentReference> qr = getUserCollection().document(uid)
                .collection("qr-link")
                .add(Map.of(
                        "creationDate", LocalDateTime.now().toString(),
                        "documentIds", Arrays.stream(documentIds).toList(),
                        "sessionType", type,
                        "sessionName", sessionName,
                        "sessionValidTime", LocalDateTime.now().plusMinutes(60L * time).toString()));

        return tokenAndPasswordUtil.generateToken(qr.get().getId(), uid);
    }

    public ResponseEntity<Map<String, Object>> accessQrLink(String token, QrLinkAccessRequest qrLinkAccessRequest) throws ExecutionException, InterruptedException {
        System.out.println(qrLinkAccessRequest.getDeviceType());
        if (!tokenAndPasswordUtil.validateToken(token))
            return new ResponseEntity<>(null, null, HttpStatus.UNAUTHORIZED);

        var userId = tokenAndPasswordUtil.getClaimFromToken(token);
        var qrId = tokenAndPasswordUtil.getSubjectFromToken(token);

        var qrDocRef = getUserCollection().document(userId).collection("qr-link").document(qrId);

        if (qrDocRef.get().get().getData().get("sessionValidTime").toString().compareTo(LocalDateTime.now().toString()) < 0)
            return new ResponseEntity<>(Map.of("message", "Session Has Expired"), null, HttpStatus.UNAUTHORIZED);

        //set last seen
        qrDocRef.update("lastSeen", LocalDateTime.now().toString());

        //update Device List
        List<Object> deviceData = (List<Object>) qrDocRef.get().get().getData().get("deviceList");
        if (deviceData != null && deviceData.contains(qrLinkAccessRequest)) {
            return new ResponseEntity<>(Map.of("message", "Device Already Exists"), null, HttpStatus.UNAUTHORIZED);
        } else {
            qrDocRef.update("deviceList", FieldValue.arrayUnion(qrLinkAccessRequest));
        }

        var qrData = qrDocRef.get().get().getData();
        var finalData = getUserCollection().document(userId).get().get().getData();

        List<Object> documentUrls = new ArrayList<>();
        List<String> documents = (List<String>) qrData.get("documentIds");
        for (var document : documents) {
            var ref = getUserCollection().document(userId).collection("Documents").document(document).get().get().getData();
            var url = documentService.downloadDocument(ref.get("documentReference").toString());
            ref.put("documentUrl", url);
            documentUrls.add(ref);
        }

        finalData.remove("password");
        finalData.put("documents", documentUrls);
        return new ResponseEntity<>(finalData, null, HttpStatus.OK);
    }


    public ArrayList<Object> getQrLinks(String uid) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getUserCollection().document(uid).collection("qr-link").get();

        var list = new ArrayList<>();
        for (DocumentSnapshot documentSnapshot : querySnapshotApiFuture.get().getDocuments()) {
            Map<String, Object> data = documentSnapshot.getData();

            List<Object> documentList = new ArrayList<>();
            for (var doc : (List<String>) data.get("documentIds")) {
                var docData = getUserCollection().document(uid).collection("Documents").document(doc).get().get().getData();
                docData.put("id", doc);
                documentList.add(docData);
            }


            data.put("documents", documentList);
            data.put("id", documentSnapshot.getId());
            list.add(data);
        }
        return list;
    }

    public void updateQrLink() {
        //update qr link
    }

    public ApiFuture<WriteResult> deleteQrLink(String uid, String qrId) throws ExecutionException, InterruptedException {
        return getUserCollection().document(uid).collection("qr-link").document(qrId).delete();
    }
}
