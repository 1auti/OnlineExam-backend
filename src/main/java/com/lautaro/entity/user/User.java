    package com.lautaro.entity.user;

    import com.lautaro.entity.persona.Persona;
    import com.lautaro.entity.rol.Role;
    import com.lautaro.entity.token.Token;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import lombok.*;
    import lombok.experimental.SuperBuilder;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.security.Principal;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.List;
    import java.util.stream.Collectors;

    import static jakarta.persistence.FetchType.EAGER;

    @Entity
    @Table(name = "users")
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @SuperBuilder
    @EntityListeners(AuditingEntityListener.class)
    public class User implements UserDetails, Principal {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @Column(unique = true)
        private String firstname;
        private String lastname;
        private String username;
        private String password;
        @Email
        @Column(unique = true)
        private String email;
        private Boolean enabled;
        private boolean accountLocked;
        @ManyToMany(fetch = EAGER)
        private List<Role> roles;
        @OneToMany(mappedBy = "user")
        private List<Token> tokens;
        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Persona persona;

        @CreatedDate
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdDate;

        @LastModifiedDate
        @Column(insertable = false)
        private LocalDateTime lastModifiedDate;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.roles
                    .stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName()))
                    .collect(Collectors.toList());
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true ;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true ;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true ;
        }

        @Override
        public boolean isEnabled() {
            return true ;
        }

        @Override
        public String getPassword() {
            return password;
        }


        @Override
        public String getName() {
            return  email;
        }

        public String fullName() {
            return getFirstname() + " " + getLastname();
        }

        public String getFullName() {
            return firstname + " " + lastname;
        }


    }
