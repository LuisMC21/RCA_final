package com.rca.RCA.auth.controller;

import com.rca.RCA.auth.dto.JwtDto;
import com.rca.RCA.auth.dto.LoginUsuario;
import com.rca.RCA.auth.service.LoginService;
import com.rca.RCA.service.UsuarioService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    LoginService loginService;

    @PostMapping("/usuario")
    public ApiResponse<UsuarioDTO> add(@RequestBody @Valid UsuarioDTO UsuarioDTO) {
        return this.loginService.add(UsuarioDTO);
    }
    @PostMapping("/login")
    public ApiResponse<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario) throws AttributeException {
        return this.loginService.login(loginUsuario);
    }
}
