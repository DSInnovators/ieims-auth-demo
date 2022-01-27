package ieims.api2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/upstream/admin")
public class UpstreamAdminController {

    private static final Logger log = LoggerFactory.getLogger(UpstreamAdminController.class);

    @GetMapping("/hello")
    public String hello(Principal principal) {
        String name = principal !=null ? principal.getName() : "";
        log.debug("hello, principal.name: {}", name);

        return "Upstream hello: " + name;
    }

}
