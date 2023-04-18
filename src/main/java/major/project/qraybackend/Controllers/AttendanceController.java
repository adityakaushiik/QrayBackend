package major.project.qraybackend.Controllers;

import major.project.qraybackend.Services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/attendance/")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    //create attendance
    @GetMapping("/create")
    public ResponseEntity<String> createAttendance(@RequestParam String userId)
            throws InterruptedException, ExecutionException {
        String attendanceId = attendanceService.createAttendance(userId);
        return ResponseEntity.ok(attendanceId);
    }

    //delete attendance
    @GetMapping("/delete")
    public ResponseEntity<Boolean> deleteAttendance(@RequestParam("userId") String userId,
                                                    @RequestParam("attendanceId") String attendanceId) {
        boolean deleted = attendanceService.deleteAttendance(userId, attendanceId);
        return ResponseEntity.ok(deleted);
    }


    //mark attendance
    @GetMapping("/mark")
    public ResponseEntity<Boolean> markAttendance(@RequestParam("userId") String userId,
                                                  @RequestParam("attendanceId") String attendanceId,
                                                  @RequestParam("attendersId") String attendersId) {
        boolean marked = attendanceService.markAttendance(userId, attendanceId, attendersId);
        return ResponseEntity.ok(marked);
    }

    //get attendance
    @GetMapping("/get")
    public ResponseEntity<Object> getAttendance(@RequestParam("userId") String userId,
                                                @RequestParam("attendanceId") String attendanceId)
            throws ExecutionException, InterruptedException {
        Object attendance = attendanceService.getAttendance(userId, attendanceId);
        return ResponseEntity.ok(attendance);
    }

    //remove attendance
    @GetMapping("/remove")
    public ResponseEntity<Boolean> removeAttendance(@RequestParam("userId") String userId,
                                                    @RequestParam("attendanceId") String attendanceId,
                                                    @RequestParam("attendersId") String attendersId) {
        boolean removed = attendanceService.removeAttendance(userId, attendanceId, attendersId);
        return ResponseEntity.ok(removed);
    }

    //get attendance by date
    //get attendance by user
    //get attendance by user and date

}
