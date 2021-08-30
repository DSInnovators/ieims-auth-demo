package ieims.api3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {


    private String client;

    public KeycloakRoleConverter(String client){
        this.client = client;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        final Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");

        final Map<String, Object> roles = (Map<String, Object>)resourceAccess.get(client);

        if(roles == null){
            List<GrantedAuthority> authorities
                    = new ArrayList<>();
            return authorities;
        }

        return ((List<String>)roles.get("roles")).stream()
                .map(roleName -> "ROLE_" + roleName) // prefix to map to a Spring Security "role"
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}