package major.project.qraybackend.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {

    @RequestMapping("/apidoc")
    public String getRedirectUrl() {
        System.out.println("Redirecting to swagger-ui");
        return "redirect:/swagger-ui/index.html#";
    }
}
