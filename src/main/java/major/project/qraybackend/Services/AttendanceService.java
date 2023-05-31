package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import major.project.qraybackend.Models.MarkAttendance;
import major.project.qraybackend.Models.SaveAttendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AttendanceService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private Firestore firestore;

    private CollectionReference getCollection() {
        return firestore.collection("users");
    }


    //create attendance
    public String createAttendance(String userId) throws ExecutionException, InterruptedException {
        Map<String, Object> attendance = new HashMap<>();
        attendance.put("creationDate", LocalDateTime.now().format(formatter));
        attendance.put("name", "Attendance");


        ApiFuture<DocumentReference> attendanceRecord =
                getCollection().document(userId).collection("attendance").add(attendance);

        return attendanceRecord.get().getId();
    }

    //delete attendance
    public boolean deleteAttendance(String userId, String attendanceId) {
        ApiFuture<WriteResult> deleting = getCollection().document(userId).collection("attendance")
                .document(attendanceId)
                .delete();

        return deleting.isDone();
    }

    //mark attendance
    public boolean markAttendance(String userId, MarkAttendance markAttendance) throws ExecutionException, InterruptedException {
//        String info = LocalDateTime.now().format(formatter) + "||" + markAttendance.getDisplayName() + "||" + markAttendance.getEmail();
        SaveAttendance saveAttendance = new SaveAttendance(
                markAttendance.getAttendersId(),
                markAttendance.getDisplayName(),
                markAttendance.getEmail(),
                LocalDateTime.now().format(formatter)
        );

        ApiFuture<DocumentReference> add = getCollection().document(userId).collection("attendance")
                .document(markAttendance.getAttendanceId()).collection("attenders").add(saveAttendance);

        return add.isDone();
    }


    //get attendance
    public List<Map<String, Object>> getAttendance(String userId, String attendanceId) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> attendanceList = new ArrayList<>();

        if (attendanceId == null || attendanceId.equals("0")) {
            CollectionReference collectionReference = getCollection().document(userId).collection("attendance");

            for (var item : collectionReference.get().get().getDocuments()) {
                var itemData = item.getData();
                itemData.put("id", item.getId());
                var attendersTotal = item.getReference().collection("attenders").get().get().size();
                itemData.put("totalAttenders", attendersTotal);

                attendanceList.add(itemData);
            }
        } else {
            getCollection().document(userId).collection("attendance")
                    .document(attendanceId).get().get()
                    .getReference()
                    .collection("attenders").get().get()
                    .getDocuments()
                    .forEach(item -> {
                        var itemData = item.getData();
                        itemData.put("recordId", item.getId());
                        attendanceList.add(itemData);
                    });
        }
        return attendanceList;
    }


    //you dont actually need to send the entire data , just send the info and the size of the attenders collection
    // according to that you can get a request for the attenders then you can print all the data in it
//    public List<Map<String, Object>> getAttendance(String userId, String attendanceId) {
//        List<Map<String, Object>> attendanceList = new ArrayList<>();
//
//        CollectionReference attendanceCollection = getCollection().document(userId).collection("attendance");
//
//        if (attendanceId == null) {
//            List<QueryDocumentSnapshot> documents;
//            try {
//                ApiFuture<QuerySnapshot> querySnapshotFuture = attendanceCollection.get();
//                QuerySnapshot querySnapshot = querySnapshotFuture.get();
//                documents = querySnapshot.getDocuments();
//            } catch (InterruptedException | ExecutionException e) {
//                // Handle the exceptions appropriately
//                e.printStackTrace();
//                return attendanceList; // or throw an exception, return an error response, etc.
//            }
//
//            List<CompletableFuture<?>> futures = documents.stream()
//                    .map(doc -> CompletableFuture.supplyAsync(() -> {
//                        Map<String, Object> attendance = doc.getData();
//                        CollectionReference attendersRef = doc.getReference().collection("attenders");
//                        List<Map<String, Object>> attenders;
//                        try {
//                            ApiFuture<QuerySnapshot> querySnapshotFuture = attendersRef.get();
//                            QuerySnapshot querySnapshot = querySnapshotFuture.get();
//                            attenders = querySnapshot.getDocuments().stream()
//                                    .map(QueryDocumentSnapshot::getData)
//                                    .collect(Collectors.toList());
//                        } catch (InterruptedException | ExecutionException e) {
//                            // Handle the exceptions appropriately
//                            e.printStackTrace();
//                            return new HashMap<>(); // or throw an exception, return an error response, etc.
//                        }
//                        attendance.put("attenders", attenders);
//                        return Map.of(doc.getId(), attendance);
//                    }))
//                    .collect(Collectors.toList());
//
//            try {
//                CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//                CompletableFuture<List<?>> allResult = allFutures.thenApply(v -> futures.stream()
//                        .map(CompletableFuture::join)
//                        .collect(Collectors.toList()));
//                attendanceList = allResult.get().stream()
//                        .map(obj -> (Map<String, Object>) obj)
//                        .collect(Collectors.toList());
//            } catch (InterruptedException | ExecutionException e) {
//                // Handle the exceptions appropriately
//                e.printStackTrace();
//                return attendanceList; // or throw an exception, return an error response, etc.
//            }
//
//        } else {
//            try {
//                DocumentSnapshot document = attendanceCollection.document(attendanceId).get().get();
//                if (document.exists()) {
//                    attendanceList.add(document.getData());
//                }
//            } catch (InterruptedException | ExecutionException e) {
//                // Handle the exceptions appropriately
//                e.printStackTrace();
//                return attendanceList; // or throw an exception, return an error response, etc.
//            }
//        }
//
//        return attendanceList;
//    }

    //get attendance
//    public List<Map<String, Object>> getAttendance(String userId, String attendanceId) throws ExecutionException, InterruptedException {
//        List<Map<String, Object>> attendanceList = new ArrayList<>();
//
//        if (attendanceId == null) {
//            CollectionReference collectionReference = getCollection().document(userId).collection("attendance");
//
//            for (var item : collectionReference.get().get().getDocuments()) {
//                var attendersReference = item.getReference().collection("attenders");
//                Map<String, Object> attendance = item.getData();
//                List<Map<String, Object>> object = new ArrayList<>();
//
//                for (var attender : attendersReference.get().get().getDocuments()) {
//                    object.add(attender.getData());
//                }
//
//                attendanceList.add(Map.of(item.getId(), attendance.put("attenders", object)));
//            }
//
//        } else {
//            Map<String, Object> attendance = getCollection().document(userId).collection("attendance")
//                    .document(attendanceId).get().get().getData();
//            attendanceList.add(attendance);
//        }
//
//        return attendanceList;
//    }

    //remove attendance
    public boolean removeAttendance(String userId, String attendanceId, String attendersId) {
        ApiFuture<WriteResult> removing = getCollection().document(userId).collection("attendance")
                .document(attendanceId)
                .update("attendance", FieldValue.arrayRemove(attendersId));
        return removing.isDone();
    }

    //get attendance by date
    //get attendance by user
    //get attendance by user and date
}
