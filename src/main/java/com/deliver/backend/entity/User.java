
package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
	public String getProfilePictureUrl() {
		// Return a default or stored profile picture URL
		// You can replace this with your actual logic
		return "";
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, length = 100)
	private String firstName;

	@Column(nullable = false, length = 100)
	private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

	@Column(nullable = false)
	@Builder.Default
	private Boolean emailVerified = false;

	@Column(nullable = false)
	@Builder.Default
	private Boolean subscribeNewsletter = false;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private UserStatus status = UserStatus.ACTIVE;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	@Builder.Default
	private Set<UserRole> roles = new HashSet<>();

	@Column(length = 20)
	private String phoneNumber;

	@Column(length = 255)
	private String address;

	private LocalDate birthDate;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime lastLoginAt;

	// Relationships
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Vendor> vendors;

	@ManyToMany
	@JoinTable(
		name = "user_favorite_products",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	@Builder.Default
	private Set<Product> favoriteProducts = new HashSet<>();

	// Enums
	public enum UserStatus {
		ACTIVE, LOCKED, DISABLED
	}

	public enum UserRole {
		USER, VENDOR, ADMIN
	}

	// Helper methods
	public String getFullName() {
		return firstName + " " + lastName;
	}

	public boolean hasRole(UserRole role) {
		return roles != null && roles.contains(role);
	}

	public void addRole(UserRole role) {
		if (roles == null) roles = new HashSet<>();
		roles.add(role);
	}

	public void removeRole(UserRole role) {
		if (roles != null) roles.remove(role);
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	// Spring Security UserDetails implementation
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		if (roles != null) {
			for (UserRole role : roles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
			}
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return email; // Spring Security uses email as username
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return status != UserStatus.LOCKED;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return status == UserStatus.ACTIVE && Boolean.TRUE.equals(emailVerified);
	}
}
