package com.revature.P1.Util.Tokens;

import com.revature.P1.Model.User;
import com.revature.P1.Util.Exceptions.InvalidTokenException;
import com.revature.P1.Util.Exceptions.UnauthorizedException;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.FileReader;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

public class JWTUtility {

    private static Properties properties = new Properties();
    private static byte[] extraSalt;

    static{
        try {
            properties.load(new FileReader("src/main/resources/db.properties"));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        extraSalt = Base64.getEncoder().encode(
                Base64.getEncoder().encode(
                        properties.getProperty("jwt-secret").getBytes())
        );
    }
    public String createToken(User user) {

        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(user.getUserName())
                .setSubject(user.getUserName())
                .setIssuer("URS-HMMW")
                .claim("position", user.getPosition())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .signWith(new SecretKeySpec(extraSalt, "HmacSHA256"));

        return tokenBuilder.compact();
    }

    public Optional<User> parseToken(String token){

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(extraSalt)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Optional.of(new User(claims.getId(), claims.getSubject(), claims.get("position").toString()));
    }

    public boolean isTokenValid(String token) {
        if(token == null || token.trim().equals("")){
            throw new InvalidTokenException();
        }
        return parseToken(token).isPresent();


    }

    public User extractTokenDetails(String token) {
        if(token == null || token.trim().equals("")){
            throw new UnauthorizedException("You have not logged in. No Token Found.");
        }
        return parseToken(token).orElseThrow(InvalidTokenException::new);
    }




}
