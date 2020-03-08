package com.prozacto.vault.security;

public class SecurityConstants {

	public static final String SECRET = "SecretKeyToGenerateJWTs";
	public static final long REFRESH_EXPIRATION_TIME = 864_000_000;
	public static final long EXPIRATION_TIME = 60*60*1000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
	public static final String REFRESH_TOKEN_URL = "/auth/refresh-token";
}
