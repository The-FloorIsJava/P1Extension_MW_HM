package com.revature.P1.Util.Exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(){
        super("Token has expired. Please log in again to create a new token" + "");
    }

}
