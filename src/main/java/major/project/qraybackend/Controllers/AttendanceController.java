package major.project.qraybackend.Controllers;

import com.google.cloud.firestore.WriteResult;
import major.project.qraybackend.Services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/attendance/")
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
    public ResponseEntity<Boolean> deleteAttendance(HttpServletRequest request,
                                                    @RequestParam("attendanceId") String attendanceId) {
        boolean deleted = attendanceService.deleteAttendance(request.getAttribute("uid").toString(), attendanceId);
        return ResponseEntity.ok(deleted);
    }

    //mark attendance
    @GetMapping("/mark")
    public ResponseEntity<WriteResult> markAttendance(HttpServletRequest request,
                                                      @RequestParam("attendanceId") String attendanceId,
                                                      @RequestParam("attendersId") String attendersId) throws ExecutionException, InterruptedException {
        WriteResult marked = attendanceService.markAttendance(request.getAttribute("uid").toString(), attendanceId, attendersId);
        return ResponseEntity.ok(marked);
    }

    //get attendance
    @GetMapping("/get")
    public ResponseEntity<Object> getAttendance(HttpServletRequest request,
                                                @RequestParam("attendanceId") String attendanceId)
            throws ExecutionException, InterruptedException {
        Object attendance = attendanceService.getAttendance(request.getAttribute("uid").toString(), attendanceId);
        return ResponseEntity.ok(attendance);
    }

    //remove attendance
    @GetMapping("/remove")
    public ResponseEntity<Boolean> removeAttendance(HttpServletRequest request,
                                                    @RequestParam("attendanceId") String attendanceId,
                                                    @RequestParam("attendersId") String attendersId) {
        boolean removed = attendanceService.removeAttendance(request.getAttribute("uid").toString(), attendanceId, attendersId);
        return ResponseEntity.ok(removed);
    }

    //get attendance by date
    //get attendance by user
    //get attendance by user and date
}
