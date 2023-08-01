package com.group3.mBaaS.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents a user of the mBaaS system
 */
@ToString
@NoArgsConstructor
public class Mbuser implements UserDetails {


    @Getter
    @Id
    public Integer id;

    private String username;

    private String password;

    @Getter
    @Setter
    private boolean enabled;

    //@Setter
    @Getter
    private Integer projectid;

    /**
     * holds the role of the user.
     * for prototype simplicity a user can only have one role
     */
    @Setter
    private Role roles;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * checks if the account is not expired
     * for prototype simplicity this method always returns false
     * @return boolean indicating the account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * checks if the account is not locked
     * for prototype simplicity this method always returns false
     * @return boolean indicating the account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * checks if the credentials are not expired
     * for prototype simplicity this method always returns false
     * @return boolean indicating if the credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    public Mbuser(String username, String password, boolean enabled, Role roles, Integer projectid) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.projectid = projectid;
    }

    /**
     * checks if the user is enabled
     * @return boolean indicating if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * gets the granted authorities of the user
     * @return a list containing the granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(roles);
        return roleList.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }


    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole(){
        return this.roles;
    }

    /**
     * get the role of a user as a list
     * for prototype simplicity a user can only have one role.
     * this list will only contain one role
     * @return a list containing the role of a user
     */
    public List<Role> getRoles() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(roles);
        return roleList;
    }
}
