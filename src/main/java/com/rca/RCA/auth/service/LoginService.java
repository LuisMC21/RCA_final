package com.rca.RCA.auth.service;

import com.rca.RCA.auth.dto.JwtDto;
import com.rca.RCA.auth.dto.LoginUsuario;
import com.rca.RCA.auth.enums.RolNombre;
import com.rca.RCA.auth.jwt.JwtProvider;
import com.rca.RCA.auth.repository.RolRepository;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.Code;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


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
    //Agregar usuario
    public ApiResponse<UsuarioDTO> add(UsuarioDTO usuarioDTO) {
        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        usuarioDTO.setId(UUID.randomUUID().toString());
        usuarioDTO.setCode(Code.generateCode(Code.USUARIO_CODE, this.usuarioRepository.count() + 1, Code.USUARIO_LENGTH));
        usuarioDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        usuarioDTO.setCreateAt(LocalDateTime.now());
        //validamos
        Optional<UsuarioEntity> optionalUsuarioEntity = this.usuarioRepository.findByNumdoc(usuarioDTO.getNumdoc());
        Optional<UsuarioEntity> optionalUsuarioEntity2 = this.usuarioRepository.findByTel(usuarioDTO.getTel());
        if (optionalUsuarioEntity.isPresent() || optionalUsuarioEntity2.isPresent()) {
            apiResponse.setSuccessful(false);
            apiResponse.setCode("Usuario_EXISTS");
            apiResponse.setMessage("No se registró, el usuario existe");
            return apiResponse;
        }
        //change dto to entity
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setUsuarioDTO(usuarioDTO);

        if(usuarioDTO.getRol().equalsIgnoreCase("ADMINISTRADOR")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_ADMIN).get());
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_TEACHER).get());
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).get());
        }
        if(usuarioDTO.getRol().equalsIgnoreCase("TEACHER")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_TEACHER).get());
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).get());
        }
        if(usuarioDTO.getRol().equalsIgnoreCase("STUDENT")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).get());
        }

        usuarioEntity.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        System.out.println(usuarioEntity);
        apiResponse.setData(this.usuarioRepository.save(usuarioEntity).getUsuarioDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

}
