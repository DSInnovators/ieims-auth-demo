package ieims.api1;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
@Configuration
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
                   
         HttpServletResponse response = (HttpServletResponse) res;
         response.setHeader("Access-Control-Allow-Origin", "*");
         response.setHeader("Access-Control-Allow-Credentials", "true");
         response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, DELETE, OPTIONS");
         response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-requested-with, X-AUTH-TOKEN, access_token, client_id, device_id, credential");
         response.setHeader("Access-Control-Max-Age", "3600");
         response.setHeader("Access-Control-Expose-Headers", "authorization, filename, location");
         if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
              response.setStatus(200);
         } else {
              chain.doFilter(request, response);
         }
    }
    @Override
    public void destroy() {
    }
}*/
