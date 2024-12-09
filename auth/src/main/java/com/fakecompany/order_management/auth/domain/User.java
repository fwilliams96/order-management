package com.fakecompany.order_management.auth.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class User extends NewUser implements UserDetails {

    private UUID id;
    private Set<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Convertir roles a SimpleGrantedAuthority
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
