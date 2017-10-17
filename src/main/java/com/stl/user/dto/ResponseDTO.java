package com.stl.user.dto;

/**
 * Created by Gihan Pc on 10/17/2017.
 */
public class ResponseDTO {
    public boolean isError;
    public String message;

    public ResponseDTO(){}

    public ResponseDTO(boolean isError, String message) {
        this.isError = isError;
        this.message = message;
    }
}
