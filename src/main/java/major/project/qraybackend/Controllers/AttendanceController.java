package major.project.qraybackend.Controllers;

import major.project.qraybackend.Models.MarkAttendance;
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
//@CrossOrigin
public class AttendanceController {
    //    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AttendanceService attendanceService;

//    public AttendanceController(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }

    //create attendance
    @GetMapping("/create")
    public ResponseEntity<Object> createAttendance(HttpServletRequest request)
            throws InterruptedException, ExecutionException {
        String attendanceId = attendanceService.createAttendance(request.getAttribute("uid").toString());

        return ResponseEntity.ok(Map.of(
                "message", "Created Successfully",
                "attendanceId", attendanceId));
    }

    //delete attendance
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAttendance(HttpServletRequest request, @RequestParam("attendanceId") String attendanceId) throws ExecutionException, InterruptedException {
        boolean deleted = attendanceService.deleteAttendance(request.getAttribute("uid").toString(), attendanceId);
        return ResponseEntity.ok(Map.of(
                "message", "Deleted Successfully",
                "writeResult", deleted)
        );
    }

    //mark attendance
    @PostMapping("/mark")
    public ResponseEntity<Map<String, Object>> markAttendance(@RequestParam("uid") String uid,
                                                              @RequestBody MarkAttendance markAttendance) throws ExecutionException, InterruptedException {
        var attendance = attendanceService.markAttendance(uid, markAttendance);
        if (attendance == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Attendance not found"));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Marked Successfully"));
    }

    //get attendance
    @GetMapping("/get")
    public ResponseEntity<List<Map<String, Object>>> getAttendance(HttpServletRequest request,
                                                                   @RequestParam(value = "attendanceId", required = false) String attendanceId) throws ExecutionException, InterruptedException {

        List<Map<String, Object>> attendance = attendanceService.getAttendance(request.getAttribute("uid").toString(), attendanceId);

        return ResponseEntity.ok(attendance);
    }

    //remove attendance
    @DeleteMapping("/remove")
    public ResponseEntity<Object> removeAttendance(HttpServletRequest request,
                                                   @RequestParam("attendanceId") String attendanceId,
                                                   @RequestParam("recordId") String recordId) {
        attendanceService.removeAttendance(request.getAttribute("uid").toString(), attendanceId, recordId);
        return ResponseEntity.ok(Map.of("message", "Removed Successfully"));
    }

    //get attendance by date
    //get attendance by user
    //get attendance by user and date
}
