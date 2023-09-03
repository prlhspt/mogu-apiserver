package com.mogu.apiserver.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorObject {

    HttpStatus getHttpStatus();
    String getMessage();

}
