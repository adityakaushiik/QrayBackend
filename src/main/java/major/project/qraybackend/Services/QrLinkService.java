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
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class QrLinkService {
    @Autowired
    private Firestore firestore;
    @Autowired
    private TokenAndPasswordUtil tokenAndPasswordUtil;

    private CollectionReference getUserCollection() {
        return firestore.collection("users");
    }

    public String createQrLink(String uid,
                               String type,
                               String sessionName,
                               int time,
                               String[] documentIds) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> qr = getUserCollection().document(uid).collection("qr-link").add(Map.of(
                "creationDate", LocalDateTime.now().toString(),
                "documentIds", Arrays.stream(documentIds).toList(),
                "sessionType", type,
                "sessionName", sessionName,
                "sessionValidTime", LocalDateTime.now().plusMinutes(60L * time).toString()
        ));

        return tokenAndPasswordUtil.generateToken(qr.get().getId(), uid);
    }

    public ResponseEntity<Map<String, Object>> accessQrLink(String token, QrLinkAccessRequest qrLinkAccessRequest) throws ExecutionException, InterruptedException {
        if (tokenAndPasswordUtil.validateToken(token)) {

            var qrData = getUserCollection().document(tokenAndPasswordUtil.getClaimFromToken(token)).collection("qr-link")
                    .document(tokenAndPasswordUtil.getSubjectFromToken(token)).get().get().getData();

            return new ResponseEntity<>(qrData, null, HttpStatus.OK);
        }
        //set device accessing
        //set last seen
        //check session validity date
        return new ResponseEntity<>(null, null, HttpStatus.UNAUTHORIZED);
    }

    public ArrayList<Object> getQrLinks(String uid) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getUserCollection().document(uid)
                .collection("qr-link").get();

        var list = new ArrayList<>();
        for (DocumentSnapshot documentSnapshot : querySnapshotApiFuture.get().getDocuments()) {
            Map<String, Object> data = documentSnapshot.getData();
            data.put("id", documentSnapshot.getId());
            list.add(data);
        }

        return list;
    }

    public void updateQrLink() {
        //update qr link
    }

    public void deleteQrLink() {
        //delete qr link
    }
}
