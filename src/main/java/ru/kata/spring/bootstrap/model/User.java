package ru.kata.spring.bootstrap.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 20, message = "First name length should be between 2 and 20")
    private String firstName;

    @Column(name = "lastName")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 20, message = "Last name length should be between 2 and 20")
    private String lastName;

    @Column(name = "age")
    @Min(value = 0, message = "Min age is 0")
    @Max(value = 100, message = "Max age is 100")
    private int age;

    @Column(name = "email")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 20, message = "Last name length should be between 2 and 20")
    private String email;

    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 20, message = "Name length should be between 2 and 20")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 1, message = "Name length should be more 1")
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(columnDefinition = "user_id"),
            inverseJoinColumns = @JoinColumn(columnDefinition = "role_id"))
    private Set<Role> roles;

    public User() {}

    public User(String firstName, String lastName, int age, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName, int age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public boolean hasRole(int roleId) {
        if (null == roles|| 0 == roles.size()) {
            return false;
        }
        Optional<Role> findRole = roles.stream().filter(role -> roleId == role.getId()).findFirst();
        return findRole.isPresent();
    }
}
