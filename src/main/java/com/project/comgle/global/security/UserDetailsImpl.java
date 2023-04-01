package com.project.comgle.global.security;

import com.project.comgle.company.entity.Company;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.entity.PositionEnum;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Member member;
    private final String username;
    private final Company company;

    public UserDetailsImpl(Member member, String username, Company company) {
        this.member = member;
        this.username = username;
        this.company = company;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        PositionEnum position = member.getPosition();

        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(position.getAuthority());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(adminAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
