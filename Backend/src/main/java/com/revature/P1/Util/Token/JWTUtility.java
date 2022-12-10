package com.revature.P1.Util.Token;

import com.revature.P1.Model.User;
import com.revature.P1.Util.Exceptions.InvalidTokenException;
import com.revature.P1.Util.Exceptions.UnauthorizedException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.Optional;
import io.jsonwebtoken.Claims;

public class JWTUtility {

    private static Properties properties = new Properties();
    private static byte[] nutySaltyBytes;

    static {
        try {
            properties.load(new FileReader("src/main/resources/db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nutySaltyBytes = Base64.getEncoder().encode(
                Base64.getEncoder().encode(properties.getProperty("jwt-secret").getBytes())
        );
    }
    public String createToken(User user) throws IOException {

        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(user.getUserName())
                .setSubject(user.getUserName())
                .setIssuer("URTS - Harsh Mehta")
                .claim("position",user.getPosition())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 8))
                .signWith(new SecretKeySpec(nutySaltyBytes, "HmacSHA256"));

        return tokenBuilder.compact();

    }

    private Optional<User> parseToken(String token){

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(nutySaltyBytes)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Optional.of(new User((claims.getId()), claims.getSubject(), claims.get("position").toString()));
    }

    public boolean isTokenValid(String token){
        if(token == null || token.trim().equals("")) return false;
        return parseToken(token).isPresent();
    }

    public User extractTokenDetails(String token){
        if(!isTokenValid(token)) {
            throw new UnauthorizedException("You've not logged in an established a token");
        }
        return parseToken(token).orElseThrow(InvalidTokenException::new);
    }



}