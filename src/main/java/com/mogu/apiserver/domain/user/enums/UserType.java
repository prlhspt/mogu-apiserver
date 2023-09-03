package com.mogu.apiserver.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mogu.apiserver.domain.user.enums.Permission.*;

@RequiredArgsConstructor
public enum UserType {

    GUEST(Collections.emptySet(), "게스트"),
    USER(
            Set.of(
                    USER_CREATE,
                    USER_READ,
                    USER_UPDATE,
                    USER_DELETE
            ),
            "일반 사용자"
    ),
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    USER_CREATE,
                    USER_READ,
                    USER_UPDATE,
                    USER_DELETE
            ),
            "관리자"
    );

    @Getter
    private final Set<Permission> permissions;
    private final String description;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
