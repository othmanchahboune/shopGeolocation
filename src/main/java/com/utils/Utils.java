package com.utils;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



public class Utils {

    private Utils() {

    }

    /**
     * Throw {@link ResponseStatusException} with BAD_REQUEST status
     * 
     * @param message
     */
    public static void throwBadRequest(String message) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }






    


}
