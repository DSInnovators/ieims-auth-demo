package ieims.api3;

//import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello( Principal principal,HttpServletRequest request) {
        String name = (String) ((JwtAuthenticationToken) principal).getTokenAttributes().get("given_name");

        return "Oauth2 response: Hello User: " + name;
    }

    @GetMapping("/upHello")
    public String upHello(Principal principal,HttpServletRequest httpServletRequest) {

        String name = (String) ((JwtAuthenticationToken) principal).getTokenAttributes().get("given_name");
        log.debug("name: {}", name);


        String token = ((JwtAuthenticationToken) principal).getToken().getTokenValue();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer "+ token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9090/upstream/admin/hello");
        String remoteValue = "";

        try {
            response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    entity, String.class);
            remoteValue =  response.getBody();
            log.debug("remoteValue: {}", remoteValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Oauth2 response: " + remoteValue;
    }

}
