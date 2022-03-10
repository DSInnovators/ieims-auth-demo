package ieims.api1;

import com.google.gson.Gson;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Helper DTO to get black listed sessions
*/
class BlackListedSession{
    private List<String> sessionIds;
    public BlackListedSession(List<String> sessionIds){
        this.sessionIds = sessionIds;
    }

    public List<String> getSessionIds() {
        return sessionIds;
    }

    public void setSessionIds(List<String> sessionIds) {
        this.sessionIds = sessionIds;
    }
}

public class CustomAuthFilter extends GenericFilterBean {
    /**
    * Redis server address
    * Will be injected from config
    */
    private final String redisHost = "localhost";
    private final Integer redisPort = 6379;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Boolean filtered = true;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        System.out.println("HTTP method: " +request.getMethod());
        System.out.println("HTTP url: " +request.getRequestURL());

        JedisPool pool = new JedisPool(redisHost, redisPort);
        try(Jedis jedis = pool.getResource()) {

            KeycloakSecurityContext ksc =
                    (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
            System.out.println(ksc.getToken());

            String userId = ksc.getToken().getSubject();
            String sessionId = ksc.getToken().getSessionId();

            String userData = jedis.get(userId);
            System.out.println("userData: " + userData);

            if(userData !=null) {

                BlackListedSession blackListedSession = new Gson().fromJson(userData,BlackListedSession.class);

                if(blackListedSession != null && blackListedSession.getSessionIds().contains(sessionId)){
                    filtered = false;
                    response.setStatus(
                            HttpServletResponse.SC_UNAUTHORIZED);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (filtered) {

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}