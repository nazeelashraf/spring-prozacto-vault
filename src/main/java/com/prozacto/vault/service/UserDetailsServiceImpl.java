package com.prozacto.vault.service;

import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser applicationUser = userRepository.findByUsername(username);
		
		if(applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		
		Collection<GrantedAuthority> authorities = 
				applicationUser.getRoles().stream().map(
						role -> {
							return role.getName();
						}).collect(Collectors.toList()).stream().map(
								SimpleGrantedAuthority::new
						).collect(Collectors.toList());
		
		return new User(applicationUser.getId().toString(), applicationUser.getPassword(), authorities);
	}

}
