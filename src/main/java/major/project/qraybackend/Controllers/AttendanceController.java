package major.project.qraybackend.Controllers;

import com.google.cloud.firestore.WriteResult;
import major.project.qraybackend.Services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/attendance/")
@CrossOrigin()
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    //create attendance
    @GetMapping("/create")
    public ResponseEntity<String> createAttendance(HttpServletRequest request)
            throws InterruptedException, ExecutionException {
        String attendanceId = attendanceService.createAttendance(request.getAttribute("uid").toString());
        return ResponseEntity.ok(attendanceId);
    }

    //delete attendance
    @GetMapping("/delete")
    public ResponseEntity<Object> deleteAttendance(HttpServletRequest request,
                                                   @RequestParam("attendanceId") String attendanceId) {
        boolean deleted = attendanceService.deleteAttendance(request.getAttribute("uid").toString(), attendanceId);
        return ResponseEntity.ok("Deleted Successfully");
    }

    //mark attendance
    @GetMapping("/mark")
    public ResponseEntity<Map<String, Object>> markAttendance(HttpServletRequest request,
                                                              @RequestParam("attendanceId") String attendanceId,
                                                              @RequestParam("attendersId") String attendersId) throws ExecutionException, InterruptedException {
        WriteResult marked = attendanceService.markAttendance(request.getAttribute("uid").toString(), attendanceId, attendersId);
        return ResponseEntity.ok(Map.of(
                "message", "Marked Successfully",
                "writeResult", marked));
    }

    //get attendance
    @GetMapping("/get")
    public ResponseEntity<List<Map<String, Object>>> getAttendance(HttpServletRequest request,
                                                                   @RequestParam(value = "attendanceId", required = false) String attendanceId) throws ExecutionException, InterruptedException {

        List<Map<String, Object>> attendance = attendanceService.getAttendance(request.getAttribute("uid").toString(), attendanceId);

        return ResponseEntity.ok(attendance);
    }

    //remove attendance
    @GetMapping("/remove")
    public ResponseEntity<Object> removeAttendance(HttpServletRequest request,
                                                   @RequestParam("attendanceId") String attendanceId,
                                                   @RequestParam("attendersId") String attendersId) {
        attendanceService.removeAttendance(request.getAttribute("uid").toString(), attendanceId, attendersId);
        return ResponseEntity.ok("Removed Successfully");
    }

    //get attendance by date
    //get attendance by user
    //get attendance by user and date
}
