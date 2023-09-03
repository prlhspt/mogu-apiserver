package com.mogu.apiserver.global.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final ErrorObject errorObject;

    public BusinessException(ErrorObject errorObject) {
        super(errorObject.getMessage());
        this.errorObject = errorObject;
    }

}
