package com.prozacto.vault.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled=true,
		securedEnabled = true
		)
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());

		http.headers().frameOptions().disable().and().cors().and().csrf().disable()
				.authorizeRequests()
					.antMatchers(HttpMethod.POST,
							SecurityConstants.SIGN_UP_URL,
							SecurityConstants.REFRESH_TOKEN_URL)
					.permitAll()
				.and().authorizeRequests()
					.antMatchers("/h2-console/**")
					.permitAll()
				.anyRequest()
					.authenticated()
					.and().addFilter(jwtAuthenticationFilter)
					.addFilter(new JWTAuthorizationFilter(authenticationManager())).sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new 
			UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		
		List<String> allowedOrigins = new ArrayList<>();
		allowedOrigins.add("http://localhost:4200");
		
		
		config.applyPermitDefaultValues();
		config.setAllowedOrigins(allowedOrigins);
		config.addExposedHeader("Authorization");
		
		source.registerCorsConfiguration("/**", config);
		
		return source;
	}
}
