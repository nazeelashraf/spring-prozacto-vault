package com.prozacto.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApplicationUser {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	@JsonIgnoreProperties
	private String password;
	private String refreshToken;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(
			name="user_roles",
			joinColumns=@JoinColumn(
					name="user_id", referencedColumnName="id"
					),
			inverseJoinColumns=@JoinColumn(
					name="role_id", referencedColumnName="id"
					)
			)
	@JsonIgnoreProperties("users")
	private Collection<Role> roles;
	
}
