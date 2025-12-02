package com.example.backend.model;

/*
 * Это одна из таблиц в базе данных. Spring Data JPA автоматически
 * создаст из этого класса таблицу в PostgreSQL
 *
 * @Entity - говорим Spring, что это класс
 * @Table - называем таблицу 'users'
 * @Id - это идентификатор конкретного пользователя в таблице
 * @GeneratedValue - позволяет автоматически генерировать идентификатор
*/

import jakarta.persistence.*;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "is_enabled", nullable = false)
  private boolean isEnabled = true;

  public enum Role {
      ROLE_USER,
      ROLE_MODERATOR,
      ROLE_ADMIN
  }

  public User() {};
  public User(String username, String password, String email, Role role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
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
    return isEnabled;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }
}
