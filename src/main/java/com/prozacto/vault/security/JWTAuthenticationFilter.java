package com.prozacto.vault.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//@NoArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	@Autowired
	public JWTAuthenticationFilter(@Lazy AuthenticationManager authenticationManager){
		super();
		setAuthenticationManager(authenticationManager);
	}

	@Autowired
	private TokenUtil tokenUtil;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			ApplicationUser creds = new ObjectMapper()
						.readValue(request.getInputStream(), ApplicationUser.class);

			return super.getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUsername(), 
							creds.getPassword())
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		User user = (User) authResult.getPrincipal();

		String token = tokenUtil.generateLoginToken(user.getUsername(), tokenUtil.authoritiesString(authResult.getAuthorities()));

		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+token);
	}
	 
}
