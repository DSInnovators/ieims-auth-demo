package ieims.api1;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @GetMapping("/hello")
    public String hello(Principal principal, HttpServletRequest request) {
        log.debug("principal: {}", principal);
        String name = principal.getName();
        log.debug("name: {}", name);

        KeycloakSecurityContext ksc =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        log.debug("ksc: {}", ksc);
        if (ksc != null) {
            log.debug("Name: {}", ksc.getToken().getName());
            log.debug("otherClaims: {}", ksc.getToken().getOtherClaims());
            log.debug("token: {}", ksc.getTokenString());
        }

        return "Hello Admin: " + name;
    }

    @GetMapping("/upHello")
    public String upHello(Principal principal, HttpServletRequest request) {
        log.debug("upHello for user: {}", principal.getName());
        String REQUEST_URI = "http://localhost:9090/upstream/admin/hello";

        KeycloakSecurityContext ksc =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        log.debug("ksc: {}", ksc);
        String accessToken = ksc.getTokenString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = keycloakRestTemplate.exchange(REQUEST_URI, HttpMethod.GET, requestEntity, String.class);
        log.debug("**** response body **** {}",response.getBody());

        return response.getBody();
    }
}
