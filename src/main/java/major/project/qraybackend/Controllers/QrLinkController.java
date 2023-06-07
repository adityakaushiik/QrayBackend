package major.project.qraybackend.Controllers;

import major.project.qraybackend.Models.QrLinkAccessRequest;
import major.project.qraybackend.Services.QrLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;


//@RestController
//@RequestMapping("api/qrLink/")
//public class QrLinkController {
//    @Autowired
//    private QrLinkService qrLinkService;
//
//    @PostMapping("/create")
//    public ResponseEntity<Object> createQrLink(HttpServletRequest request,
//                                               @RequestParam("type") String type,
//                                               @RequestParam("sessionName") String sessionName,
//                                               @RequestParam(value = "validTime", required = false) int time,
//                                               @RequestBody String[] documentIds)
//            throws ExecutionException, InterruptedException {
//
//        String qrLink = qrLinkService.createQrLink(
//                request.getAttribute("uid").toString(),
//                type,
//                sessionName,
//                time,
//                documentIds);
//        return ResponseEntity.ok(Map.of("token", qrLink));
//    }
//
//
//    @PostMapping("/access")
//    public ResponseEntity<Map<String, Object>> accessQrLink(@RequestBody QrLinkAccessRequest qrLinkAccessRequest,
//                                                            @RequestParam("token") String token)
//            throws ExecutionException, InterruptedException {
//
//        System.out.println(qrLinkAccessRequest.getIpAddress());
//        return qrLinkService.accessQrLink(token, qrLinkAccessRequest);
//    }
//
//
//    @GetMapping("/get")
//    public ResponseEntity<ArrayList<Object>> getQrLink(HttpServletRequest request)
//            throws ExecutionException, InterruptedException {
//
//        ArrayList<Object> data = qrLinkService.getQrLinks(request.getAttribute("uid").toString());
//        return ResponseEntity.ok(data);
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<Object> deleteQrLink(HttpServletRequest request,
//                                               @RequestParam("qrId") String qrId)
//            throws ExecutionException, InterruptedException {
//
//        var write = qrLinkService.deleteQrLink(request.getAttribute("uid").toString(), qrId);
//        return ResponseEntity.ok(Map.of("message", "Deleted",
//                "write", write.get().getUpdateTime().toString()));
//    }
//}

@RestController
@RequestMapping("api/qrLink/")
public class QrLinkController {
    @Autowired
    private QrLinkService qrLinkService;

    @PostMapping("/create")
    public ResponseEntity<Object> createQrLink(HttpServletRequest request,
                                               @RequestParam("type") String type,
                                               @RequestParam("sessionName") String sessionName,
                                               @RequestParam(value = "validTime", required = false) int time,
                                               @RequestBody String[] documentIds) {
        try {
            String qrLink = qrLinkService.createQrLink(
                    request.getAttribute("uid").toString(),
                    type,
                    sessionName,
                    time,
                    documentIds);
            return ResponseEntity.ok(Map.of("token", qrLink));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/access")
    public ResponseEntity<Map<String, Object>> accessQrLink(@RequestBody QrLinkAccessRequest qrLinkAccessRequest,
                                                            @RequestParam("token") String token) {
        try {
            System.out.println(qrLinkAccessRequest.getIpAddress());
            return qrLinkService.accessQrLink(token, qrLinkAccessRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<ArrayList<Object>> getQrLink(HttpServletRequest request) {
        try {
            ArrayList<Object> data = qrLinkService.getQrLinks(request.getAttribute("uid").toString());
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteQrLink(HttpServletRequest request,
                                               @RequestParam("qrId") String qrId) {
        try {
            var write = qrLinkService.deleteQrLink(request.getAttribute("uid").toString(), qrId);
            return ResponseEntity.ok(Map.of("message", "Deleted", "write", write.get().getUpdateTime().toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
