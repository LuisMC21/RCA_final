package com.rca.RCA.util.exceptions;

import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.util.Operations;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode("NOT_FOUND");
        return apiResponse;
    }
    //Excepción cuando el atributo está repetido
    @ExceptionHandler(AttributeException.class)
    public ApiResponse<Exception> throwAttributeException(AttributeException e){
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        apiResponse.setCode("BAD_REQUEST");
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(e.getMessage());
        return apiResponse;
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<Exception> handleBadCredentialsException(BadCredentialsException e){
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(false);
        apiResponse.setMessage("Usuario o contraseña incorrectos");
        apiResponse.setCode("BadCredentialsException" + e.getMessage());
        return apiResponse;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Exception> handleAuthenticationException(AuthenticationException e) {
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(false);
        apiResponse.setMessage("Usuario o contraseña incorrectos");
        apiResponse.setCode("AuthenticationException" + e.getMessage());
        return apiResponse;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Exception> handleAccessDeniedException(AccessDeniedException e){
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode("AccessDeniedException");
        return apiResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Exception> validationException(MethodArgumentNotValidException e){
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((err) -> messages.add(err.getDefaultMessage()));
        apiResponse.setCode("BAD_REQUEST");
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(Operations.trimBrackets(messages.toString()));
        return apiResponse;
    }

    @ExceptionHandler(value = {MalformedJwtException.class, UnsupportedJwtException.class, IllegalArgumentException.class, SignatureException.class})
    public ApiResponse<Exception> jwtException(JwtException e){
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode("BadCredentialsException");
        return apiResponse;
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Exception> generalException(Exception e){
        ApiResponse<Exception> apiResponse = new ApiResponse<>();
        apiResponse.setSuccessful(false);
        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode("INTERNAL_SERVER_ERROR");
        return apiResponse;
    }
}
