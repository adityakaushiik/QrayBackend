package major.project.qraybackend.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Object user = new Object();
        ApiFuture<DocumentReference> attendance =
                getCollection().document(userId).collection("attendance").add(user);
        return attendance.get().getId();
    }

    //delete attendance
    public boolean deleteAttendance(String userId, String attendanceId) {
        ApiFuture<WriteResult> deleting = getCollection().document(userId).collection("attendance")
                .document(attendanceId)
                .delete();
        return deleting.isDone();
    }

    //mark attendance
    public boolean markAttendance(String userId, String attendanceId, String attendersId) {
        ApiFuture<WriteResult> marking = getCollection().document().collection("attendance")
                .document(attendanceId)
                .update("attendance", FieldValue.arrayUnion(userId));
        return marking.isDone();
    }

    //get attendance
    public Object getAttendance(String userId, String attendanceId) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> attendance = getCollection().document(userId).collection("attendance")
                .document(attendanceId)
                .get();
        return attendance.get().getData();
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
