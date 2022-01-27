package ieims.api1;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

@KeycloakConfiguration
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    /**
     * Registers KeycloakAuthenticationProvider with spring security's authentication manager.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authBuilder) {
        KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();

        // Setting this so the adapter automatically adds ROLE_ prefix to roles defined in Keycloak
        // Spring Security expects ROLE_ prefixes in all roles
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

        authBuilder.authenticationProvider(provider);
    }

    /**
     * This is provided by the Keycloak adapter library.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    /**
     * Use Spring Boot native configuration for Keycloak.
     */
    @Bean
    public KeycloakConfigResolver configResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Define session authentication strategy. As this is a bearer-only client, we
     * are using the NullAuthenticatedSessionStrategy.
     */
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    /**
     * Global authorization configuration. Also, enables Spring Security CORS handling.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/upHello").hasRole("SUPER_ADMIN")
                .antMatchers("/user/*").hasRole("USER")
                .anyRequest().denyAll();
        http.cors();
    }

    /**
     * Provide CORS configuration for Spring Security. Allowing all origins and methods for now.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Helpful template to call upstream service providers passing the access token.
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

}
