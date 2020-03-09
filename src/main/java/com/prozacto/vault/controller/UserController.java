package com.prozacto.vault.controller;

import com.prozacto.vault.exception.EmptyFieldException;
import com.prozacto.vault.exception.UserAlreadyExistsException;
import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.repository.RoleRepository;
import com.prozacto.vault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/sign-up")
	public String signUp(@RequestBody ApplicationUser user) {

		if(user.getPassword()==null) throw new EmptyFieldException("password");
		if(user.getUsername()==null) throw new EmptyFieldException("username");

		if(userRepository.findByUsername(user.getUsername()) != null) {
			throw new UserAlreadyExistsException();
		}

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
		user = userRepository.save(user);

		return "Thank you for signing up! Your unique ID is "+user.getId();
	}
	
	@GetMapping("/all")
	@Secured({"ROLE_ADMIN"})
	public List<ApplicationUser> findAll(){
		return userRepository.findAll();
	}
	
}
