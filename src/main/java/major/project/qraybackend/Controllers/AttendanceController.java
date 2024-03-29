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
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/create")
    public ResponseEntity<Object> createAttendance(HttpServletRequest request, @RequestParam(value = "name", required = false) String name)
            throws InterruptedException, ExecutionException {
        String attendanceId = attendanceService.createAttendance(request.getAttribute("uid").toString(), name);

        return ResponseEntity.ok(Map.of(
                "message", "Created Successfully",
                "attendanceId", attendanceId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAttendance(HttpServletRequest request, @RequestParam("attendanceId") String attendanceId) throws ExecutionException, InterruptedException {
        boolean deleted = attendanceService.deleteAttendance(request.getAttribute("uid").toString(), attendanceId);
        return ResponseEntity.ok(Map.of(
                "message", "Deleted Successfully",
                "writeResult", deleted)
        );
    }

    @PostMapping("/mark")
    public ResponseEntity<Map<String, Object>> markAttendance(HttpServletRequest request,
                                                              @RequestParam("uid") String uid,
                                                              @RequestBody MarkAttendance markAttendance) throws ExecutionException, InterruptedException {
        var attendance = attendanceService.markAttendance(uid, markAttendance);
        if (attendance == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Attendance not found"));
        }
        return ResponseEntity.ok(Map.of(
                "message", markAttendance.getDisplayName() + " Marked Successfully"));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Map<String, Object>>> getAttendance(HttpServletRequest request,
                                                                   @RequestParam(value = "attendanceId", required = false) String attendanceId) throws ExecutionException, InterruptedException {

        List<Map<String, Object>> attendance = attendanceService.getAttendance(request.getAttribute("uid").toString(), attendanceId);

        return ResponseEntity.ok(attendance);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Object> removeAttendance(HttpServletRequest request,
                                                   @RequestParam("attendanceId") String attendanceId,
                                                   @RequestParam("recordId") String recordId) {
        attendanceService.removeAttendance(request.getAttribute("uid").toString(), attendanceId, recordId);
        return ResponseEntity.ok(Map.of("message", "Removed Successfully"));
    }
}
