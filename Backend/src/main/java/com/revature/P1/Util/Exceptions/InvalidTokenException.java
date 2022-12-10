package com.revature.P1.Util.Exceptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("Token expired please log in again to generate new token" +
                "");
    }
}
