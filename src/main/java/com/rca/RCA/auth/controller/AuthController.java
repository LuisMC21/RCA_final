package com.rca.RCA.auth.controller;

import com.rca.RCA.auth.dto.JwtDto;
import com.rca.RCA.auth.dto.LoginUsuario;
import com.rca.RCA.auth.service.LoginService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    LoginService loginService;

    @PostMapping("/usuario")
    public ApiResponse<UsuarioDTO> add(@RequestBody @Valid UsuarioDTO UsuarioDTO) throws AttributeException, ResourceNotFoundException {
        return this.loginService.add(UsuarioDTO);
    }
    @PostMapping("/login")
    public ApiResponse<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario) throws ResourceNotFoundException {
        return this.loginService.login(loginUsuario);
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        return this.loginService.refresh(jwtDto);
    }
}
