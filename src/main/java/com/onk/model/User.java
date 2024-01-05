package com.onk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onk.core.utils.DbConstants;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = DbConstants.userTableName)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseModel implements UserDetails {

    private static final long serialVersion = 1L;

    @Column(name = DbConstants.userEmail, unique = true,
            nullable = false)
    @Size(max = DbConstants.textVentiSize)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Column(name = DbConstants.userName, length = DbConstants.textTallSize)
    private String name;

    @NotBlank
    @Column(name = DbConstants.userLastName, length = DbConstants.textGrandeSize)
    private String lastName;

    @NotBlank
    @JsonIgnore
    @Column(name = DbConstants.userPassword)
    @Size(min = DbConstants.minPasswordSize, max = DbConstants.textVentiSize)
    private String password;

    @Column(name = DbConstants.userIsActive)
    private Boolean isActive;

    @Column(name = DbConstants.userIsDeleted)
    private Boolean isDeleted;

    @Size(max = DbConstants.textLongContentSize)
    @Column(name = DbConstants.userForCancel)
    private String forCancel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = DbConstants.userRoleTableName,
            joinColumns = @JoinColumn(name = DbConstants.userRoleTableNameUserId),
            inverseJoinColumns = @JoinColumn(name = DbConstants.userRoleTableNameRoleId)
    )
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    public User(String email, String name, String lastName,
                String password, @Nullable Boolean isActive) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(e -> new SimpleGrantedAuthority("ROLE_" + e.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getEmail();
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

