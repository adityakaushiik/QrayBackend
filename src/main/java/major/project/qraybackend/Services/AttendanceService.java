package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AttendanceService {
    @Autowired
    private Firestore firestore;

    private CollectionReference getCollection() {
        return firestore.collection("users");
    }


    //create attendance
    public String createAttendance(String userId) throws ExecutionException, InterruptedException {
        Map<String, Object> attendance = new HashMap<>();
        attendance.put("attendance", new ArrayList<>());
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
    public WriteResult markAttendance(String userId, String attendanceId, String attendersId) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> marking = getCollection().document(userId).collection("attendance")
                .document(attendanceId)
                .update("attendance", FieldValue.arrayUnion(attendersId));
        return marking.get();
    }

    //get attendance
    public List<Map<String, Object>> getAttendance(String userId, String attendanceId) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> attendanceList = new ArrayList<>();
        if (attendanceId == null) {
            List<QueryDocumentSnapshot> attendance = getCollection().document(userId).collection("attendance")
                    .get().get().getDocuments();

            for (QueryDocumentSnapshot documentSnapshot : attendance)
                attendanceList.add(Map.of(documentSnapshot.getId(), documentSnapshot.getData().get("attendance")));

            return attendanceList;
        }

        ApiFuture<DocumentSnapshot> attendance = getCollection().document(userId).collection("attendance")
                .document(attendanceId)
                .get();
        attendanceList.add(Map.of(attendance.get().getId(), attendance.get().getData().get("attendance")));
        return attendanceList;
    }

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
