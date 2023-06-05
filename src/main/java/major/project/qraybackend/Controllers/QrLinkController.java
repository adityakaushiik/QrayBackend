package major.project.qraybackend.Controllers;

import major.project.qraybackend.Models.QrLinkAccessRequest;
import major.project.qraybackend.Services.QrLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("api/qrLink/")
public class QrLinkController {
    @Autowired
    private QrLinkService qrLinkService;

    @PostMapping("/create")
    public ResponseEntity<String> createQrLink(HttpServletRequest request,
                                               @RequestParam("type") String type,
                                               @RequestParam("sessionName") String sessionName,
                                               @RequestParam(value = "validTime", required = false) int time,
                                               @RequestBody String[] documentIds)
            throws ExecutionException, InterruptedException {

        String qrLink = qrLinkService.createQrLink(
                request.getAttribute("uid").toString(),
                type,
                sessionName,
                time,
                documentIds);
        return ResponseEntity.ok(qrLink);
    }


    @PostMapping("/access")
    public ResponseEntity<Map<String, Object>> accessQrLink(@RequestBody QrLinkAccessRequest qrLinkAccessRequest,
                                                            @RequestParam("token") String token)
            throws ExecutionException, InterruptedException {

        System.out.println(qrLinkAccessRequest.getIpAddress());
        return qrLinkService.accessQrLink(token, qrLinkAccessRequest);
    }


    @GetMapping("/get")
    public ResponseEntity<ArrayList<Object>> getQrLink(HttpServletRequest request)
            throws ExecutionException, InterruptedException {

        ArrayList<Object> data = qrLinkService.getQrLinks(request.getAttribute("uid").toString());
        return ResponseEntity.ok(data);
    }
}
