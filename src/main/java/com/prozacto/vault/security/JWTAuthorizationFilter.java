package com.prozacto.vault.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.prozacto.vault.util.TokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
		super(authenticationManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if(header==null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
		
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if(token != null) {
			DecodedJWT jwt = TokenUtil.decodeToken(token);
			
			String user = jwt.getSubject();
			final Collection<GrantedAuthority> authorities = 
					Arrays.stream(jwt.getClaim("ROLES").asString().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
			
			if(user != null) {
				return new UsernamePasswordAuthenticationToken(user, "", authorities);
			}
			
			return null;
		}
		
		return null;
	}
}
