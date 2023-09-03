package com.mogu.apiserver.domain.account;

import com.mogu.apiserver.domain.user.enums.UserType;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AccountPrincipal implements UserDetails {

    private final String username;
    private final String password;
    private final UserType userTypes;


    @Builder
    public AccountPrincipal(String username, String password, UserType userTypes) {
        this.username = username;
        this.password = password;
        this.userTypes = userTypes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userTypes.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
