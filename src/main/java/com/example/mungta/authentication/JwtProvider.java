package com.example.mungta.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtProvider {
    private final static String SECRET_KEY =  "MungTa03Service";

    private DecodedJWT decodeToken(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build();
        return verifier.verify(token);
    }

    public String pickToken(String token){
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }else{
            return "";
        }
    }

    public Map<String, Claim> getAllClaimsFromToken(String token) throws JWTVerificationException {
        return decodeToken(pickToken(token)).getClaims();
    }

    public <T> T getClaimFromToken(String token, Function<Map<String, Claim>, T> claimsResolver) throws JWTVerificationException{
        final Map<String, Claim> claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Long getExpirationDateFromToken(String token) throws JWTVerificationException{
        return getClaimFromToken(token, claimMap->claimMap.get("exp")).asLong();
    }

    public Boolean isTokenExpired(String token) {
        try{
            final Date expiration = new Date(getExpirationDateFromToken(token)*1000L);
            return expiration.before(new Date());

        }catch(JWTVerificationException verificationEx){
            System.out.println("Verify Error");
            verificationEx.printStackTrace();
            return true;
        }
    }

}
