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
@CrossOrigin("http://localhost:4200/")
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
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAttendance(HttpServletRequest request,
                                                   @RequestParam("attendanceId") String attendanceId) {
        boolean deleted = attendanceService.deleteAttendance(request.getAttribute("uid").toString(), attendanceId);
        return ResponseEntity.ok("Deleted Successfully");
    }

    //mark attendance
    @PostMapping("/mark")
    public ResponseEntity<Map<String, Object>> markAttendance(HttpServletRequest request,
                                                              @RequestBody MarkAttendance markAttendance) throws ExecutionException, InterruptedException {


        boolean marked = attendanceService.markAttendance(request.getAttribute("uid").toString(), markAttendance);

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
    @DeleteMapping("/remove")
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
