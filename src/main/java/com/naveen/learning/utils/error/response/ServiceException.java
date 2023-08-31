package com.naveen.learning.utils.error.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException implements Serializable {

    private final static long serialVersionUID = 4613301307200511995L;

    private ErrorInfo errorInfo;

    private HttpStatus httpStatus;

    public ServiceException(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
