package com.prozacto.vault.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prozacto.vault.exception.InvalidRefreshTokenException;
import com.prozacto.vault.exception.InvalidRoleException;
import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.model.Doctor;
import com.prozacto.vault.model.Patient;
import com.prozacto.vault.repository.AssistantRepository;
import com.prozacto.vault.repository.DoctorRepository;
import com.prozacto.vault.repository.PatientRepository;
import com.prozacto.vault.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.digest.SHA1;
import org.bouncycastle.jcajce.provider.digest.SHA512;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserDetailsService userDetailsService;


    public String generateLoginToken(String id, String authorities) {

        ApplicationUser applicationUser = userRepository.findById(Long.parseLong(id)).get();

        String refreshToken = generateRefreshToken();

        String token = JWT.create()
                .withSubject(applicationUser.getId().toString())
                .withClaim("ROLES", authorities)
                .withClaim("refreshExp", new Date(System.currentTimeMillis()+ SecurityConstants.REFRESH_EXPIRATION_TIME))
                .withClaim("refreshToken", refreshToken)
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        applicationUser.setRefreshToken(refreshToken);

        userRepository.save(applicationUser);

        return token;
    }

    public Long getUserId(String token){
        token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

        DecodedJWT jwt = JWT.decode(token);
        return Long.parseLong(jwt.getSubject());
    }

    public Long getClinicId(String token){
        token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

        DecodedJWT jwt = JWT.decode(token);
        List<String> roles = Arrays.asList(jwt.getClaim("ROLES").asString().split(","));

        if(roles.contains("ROLE_DOCTOR")){
            return doctorRepository.getClinicId(Long.parseLong(jwt.getSubject()));
        } else if (roles.contains("ROLE_ASSISTANT")) {
            return assistantRepository.getClinicId(Long.parseLong(jwt.getSubject()));
        } else {
            throw new InvalidRoleException();
        }
    }

    public List<Long> getClinicIdList(String token){
        token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

        DecodedJWT jwt = JWT.decode(token);
        List<String> roles = Arrays.asList(jwt.getClaim("ROLES").asString().split(","));

        if (roles.contains("ROLE_PATIENT")) {
            Patient patient = patientRepository.findPatientByUserId(Long.parseLong(jwt.getSignature()));
            return patientRepository.getClinicIdList(patient.getPatientId());
        } else {
            throw new InvalidRoleException();
        }
    }

    public List<String> getRoles(String token){
        token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

        DecodedJWT jwt = JWT.decode(token);
        List<String> roles = Arrays.asList(jwt.getClaim("ROLES").asString().split(","));

        return roles;
    }

    public static DecodedJWT decodeToken(String token) {
        DecodedJWT jwt =
                JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
        return  jwt;
    }

    public static String authoritiesString(Collection<? extends GrantedAuthority> authorities){
        final String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return authoritiesString;
    }

    private String generateRefreshToken(){
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public String refreshToken(String token) throws InvalidRefreshTokenException{
        DecodedJWT jwt;
        try{
            jwt = decodeToken(token);
        } catch(TokenExpiredException exception){
            jwt = JWT.decode(token);
            if(jwt.getClaim("refreshExp").asDate().before(new Date()))
                throw new TokenExpiredException("Refresh token has expired. Please login again");

            ApplicationUser user = userRepository.findById(Long.parseLong(jwt.getSubject())).get();

            if(!jwt.getClaim("refreshToken").asString().equals(user.getRefreshToken()))
                throw new InvalidRefreshTokenException();

            String username = jwt.getSubject();
            String authorities = jwt.getClaim("ROLES").asString();

            token = generateLoginToken(username, authorities);

        } catch (SignatureVerificationException | AlgorithmMismatchException | InvalidClaimException e){
            throw e;
        }

        return token;

    }


}
