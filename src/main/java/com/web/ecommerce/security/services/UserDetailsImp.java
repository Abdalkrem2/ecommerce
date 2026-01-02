package com.web.ecommerce.security.services;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.ecommerce.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDetailsImp implements UserDetails {
private static final long serialVersionUID = 1L;
private Long id;
private String username;
private String email;
@JsonIgnore//إذا ما استخدمت @JsonIgnore، رح يطلع الـ password بالـ JSON response.
private String password;
private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImp(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImp build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());


        return new UserDetailsImp(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(),authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//"This method returns a collection of objects that are of some type that implements GrantedAuthority."
        return authorities;
    }

    @Override
    public String getPassword() {
        return password ;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        UserDetailsImp userDetailsImp = (UserDetailsImp) o;
        return Objects.equals(id, userDetailsImp.id);
    }
}
