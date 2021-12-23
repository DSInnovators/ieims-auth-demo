package ieims.api1;

import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/hello")
    public String hello(Principal principal, HttpServletRequest request) {
        String name = principal.getName();

        KeycloakSecurityContext ksc =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        if (ksc != null) {
            log.debug("Name: {}", ksc.getToken().getName());
            log.debug("otherClaims: {}", ksc.getToken().getOtherClaims());
            log.debug("token: {}", ksc.getTokenString());
        }
        return "Hello User: " + name;
    }

}
