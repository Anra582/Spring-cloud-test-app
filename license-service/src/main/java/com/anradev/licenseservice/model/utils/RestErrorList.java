package com.anradev.licenseservice.model.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static java.util.Arrays.asList;

@Getter
@Setter
public class RestErrorList extends ArrayList<ErrorMessage> {

 private HttpStatus status;

    public RestErrorList(HttpStatus status, ErrorMessage... errors) {
        super();
        this.status = status;
        addAll(asList(errors));
    }

    public RestErrorList(int status, ErrorMessage... errors) {
        this(HttpStatus.valueOf(status), errors);
    }
}
