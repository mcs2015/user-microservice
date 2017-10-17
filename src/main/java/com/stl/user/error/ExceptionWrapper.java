package com.stl.user.error;

public class ExceptionWrapper {

    public ErrorCode errorCode;

    public String message;

    public ExceptionWrapper(ErrorCode errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ExceptionWrapper{" +
                "message='" + message + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
