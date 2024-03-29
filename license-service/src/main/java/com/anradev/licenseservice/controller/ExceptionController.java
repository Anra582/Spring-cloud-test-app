package com.anradev.licenseservice.controller;

import com.anradev.licenseservice.model.utils.ErrorMessage;
import com.anradev.licenseservice.model.utils.ResponseWrapper;
import com.anradev.licenseservice.model.utils.RestErrorList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Collections.singletonMap;

@ControllerAdvice
@EnableWebMvc
public class ExceptionController extends ResponseEntityExceptionHandler {

    /**
     * handleException - Handles all the Exception receiving a request, responseWrapper.
     *@param request
     *@param responseWrapper
     *@return ResponseEntity<ResponseWrapper>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper> handleException(HttpServletRequest request,
                                                           ResponseWrapper responseWrapper){
        return ResponseEntity.ok(responseWrapper);
    }

    /**
     * handleIOException - Handles all the Authentication Exceptions of the application.
     *@param request
     *@param e
     *@return ResponseEntity<ResponseWrapper>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseWrapper> handleIOException(HttpServletRequest request, RuntimeException e){

        RestErrorList errorList = new RestErrorList(HttpStatus.NOT_ACCEPTABLE, new ErrorMessage(e.getMessage()));
        ResponseWrapper responseWrapper = new ResponseWrapper(null, singletonMap("status", HttpStatus.NOT_ACCEPTABLE), errorList);

        return ResponseEntity.ok(responseWrapper);
    }
}
