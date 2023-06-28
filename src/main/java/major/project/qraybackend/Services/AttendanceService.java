package major.project.qraybackend.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private CollectionReference getCollection(String userId) {
        return firestore.collection("users").document(userId).collection("attendance");
    }

    // Create attendance
    public String createAttendance(String userId, String name) throws ExecutionException, InterruptedException {
        Map<String, Object> attendance = new HashMap<>();
        attendance.put("creationDate", LocalDateTime.now().format(formatter));
        if (name == null || name.equals("0"))
            attendance.put("name", "Attendance");
        else
            attendance.put("name", name);
        DocumentReference attendanceRecord = getCollection(userId).add(attendance).get();

        return attendanceRecord.getId();
    }

    // Delete attendance
    public boolean deleteAttendance(String userId, String attendanceId) {
        ApiFuture<WriteResult> deleting = getCollection(userId).document(attendanceId).delete();

        return deleting.isDone();
    }

    // Mark attendance
    public Map markAttendance(String userId, MarkAttendance markAttendance) throws ExecutionException, InterruptedException {
        SaveAttendance saveAttendance = new SaveAttendance(
                markAttendance.getAttendersId(),
                markAttendance.getDisplayName(),
                markAttendance.getEmail(),
                LocalDateTime.now().format(formatter)
        );
        if (getCollection(userId).document(markAttendance.getAttendanceId()).get().get().getData() == null) {
            return null;
        }
        ApiFuture<DocumentReference> add = getCollection(userId)
                .document(markAttendance.getAttendanceId())
                .collection("attenders")
                .add(saveAttendance);

        var attendance = new ObjectMapper().convertValue(saveAttendance, Map.class);
        attendance.put("id", add.get().getId());

        return attendance;
    }

    // Get attendance
    public List<Map<String, Object>> getAttendance(String userId, String attendanceId) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> attendanceList = new ArrayList<>();

        if (attendanceId == null || attendanceId.equals("0")) {
            QuerySnapshot attendanceSnapshot = getCollection(userId).get().get();
            for (DocumentSnapshot item : attendanceSnapshot.getDocuments()) {
                Map<String, Object> itemData = item.getData();
                itemData.put("id", item.getId());

                CollectionReference attendersCollection = item.getReference().collection("attenders");
                int attendersTotal = attendersCollection.get().get().size();
                itemData.put("totalAttenders", attendersTotal);

                attendanceList.add(itemData);
            }
        } else {
            CollectionReference attendersCollection = getCollection(userId)
                    .document(attendanceId)
                    .collection("attenders");

            QuerySnapshot attendersSnapshot = attendersCollection.get().get();
            for (DocumentSnapshot item : attendersSnapshot.getDocuments()) {
                Map<String, Object> itemData = item.getData();
                itemData.put("recordId", item.getId());
                attendanceList.add(itemData);
            }
        }

        return attendanceList;
    }

    // Remove attendance
    public boolean removeAttendance(String userId, String attendanceId, String recordId) {
        ApiFuture<WriteResult> removing = getCollection(userId)
                .document(attendanceId)
                .collection("attenders")
                .document(recordId)
                .delete();

        return removing.isDone();
    }
}