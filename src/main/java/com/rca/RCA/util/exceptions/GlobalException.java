package com.rca.RCA.util.exceptions;

import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.util.Operations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
    //Excepción cuando no encuentra el objeto
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Exception> throwNotFoundException(ResourceNotFoundException e){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode("NOT_FOUND");
        return apiResponse;
    }
    //Excepción cuando el atributo está repetido
    @ExceptionHandler(AttributeException.class)
    public ApiResponse<Exception> throwAttributeException(AttributeException e){
        ApiResponse apiResponse = new ApiResponse();
        if(HttpStatus.BAD_REQUEST.isError()) {
            apiResponse.setCode("BAD_REQUEST");
            apiResponse.setSuccessful(false);
            apiResponse.setMessage(e.getMessage());
        }
        return apiResponse;
    }
    @ExceptionHandler(Exception.class)
    public ApiResponse<Exception> generalException(Exception e){
            ApiResponse apiResponse = new ApiResponse();
        if(ResponseEntity.internalServerError()!=null) {
            apiResponse.setSuccessful(false);
            apiResponse.setMessage(e.getMessage());
            apiResponse.setCode("INTERNAL_SERVER_ERROR");
        }
        return apiResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Exception> validationException(MethodArgumentNotValidException e){
        ApiResponse apiResponse = new ApiResponse();
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((err) ->{
            messages.add(err.getDefaultMessage());
        });
        apiResponse.setCode("BAD_REQUEST");
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(Operations.trimBrackets(messages.toString()));
        return apiResponse;
    }
}
