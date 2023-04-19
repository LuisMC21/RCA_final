package com.rca.RCA.auth.service;

import com.rca.RCA.auth.dto.JwtDto;
import com.rca.RCA.auth.dto.LoginUsuario;
import com.rca.RCA.auth.jwt.JwtProvider;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class LoginService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse<JwtDto> login(LoginUsuario loginUsuario) throws AttributeException {
        ApiResponse apiResponse = new ApiResponse();
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto();
        jwtDto.setToken(jwt);
        jwtDto.setEmailorUser(loginUsuario.getNombreUsuario());
        apiResponse.setMessage("Sesión correcta");
        apiResponse.setData(jwtDto);
        apiResponse.setCode("Bearer");
        apiResponse.setSuccessful(true);
        return apiResponse;
    }
}
