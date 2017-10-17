package com.stl.user.util;


import com.google.common.hash.Hashing;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.StandardCharsets;

public class STLValidator {

    public static boolean validatePassword(String password){
        if(StringUtils.isEmpty(password)){
            //password cannot be blank
            return false;
        }

        if(password.length() < Constants.MIN_PASSWORD_LENGTH){
            //password does not have the required minimum length
            return false;
        }

        if(!StringUtils.isAlphanumeric(password)){
            //password needs to have alpha numeric characters
            return false;
        }
        return true;
    }


    public static String hashPassword(String passwordToHash){
        if(StringUtils.isEmpty(passwordToHash)){
            return Constants.EMPTY_STRING;
        }
        return Hashing.sha256()
                .hashString(passwordToHash, StandardCharsets.UTF_8)
                .toString();

    }
}
