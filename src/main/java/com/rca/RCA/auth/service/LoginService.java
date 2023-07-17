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
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.UUID;
@Log4j2

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


    public ApiResponse<JwtDto> login(LoginUsuario loginUsuario) throws ResourceNotFoundException {
        log.info("Entró a validar el logueo");
        ApiResponse<JwtDto> apiResponse = new ApiResponse<>();
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto();
        jwtDto.setToken(jwt);
        apiResponse.setMessage("Sesión correcta");
        apiResponse.setData(jwtDto);
        apiResponse.setCode("Bearer");
        apiResponse.setSuccessful(true);
        return apiResponse;
    }
    //Agregar usuario
    public ApiResponse<UsuarioDTO> add(UsuarioDTO usuarioDTO) throws AttributeException, ResourceNotFoundException {
        //Excepciones
        if(this.usuarioRepository.existsByNumdoc(usuarioDTO.getNumdoc(), "", ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Usuario con documento existente");
        if(this.usuarioRepository.existsByTel(usuarioDTO.getTel(), "", ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Usuario con telefono existente");
        if(this.usuarioRepository.existsByEmail(usuarioDTO.getEmail(), "", ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Usuario con email existente");
        if(this.usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario(), "", ConstantsGeneric.CREATED_STATUS))
            throw new AttributeException("Usuario con nombre de usuario existente");

        ApiResponse<UsuarioDTO> apiResponse = new ApiResponse<>();
        usuarioDTO.setId(UUID.randomUUID().toString());
        usuarioDTO.setCode(Code.generateCode(Code.USUARIO_CODE, this.usuarioRepository.count() + 1, Code.USUARIO_LENGTH));
        usuarioDTO.setStatus(ConstantsGeneric.CREATED_STATUS);
        usuarioDTO.setCreateAt(LocalDateTime.now());
        log.info("Creó los datos de auditoría");
        //change dto to entity
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setUsuarioDTO(usuarioDTO);

        if(usuarioDTO.getRol().equalsIgnoreCase("ADMINISTRADOR")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_ADMIN).orElseThrow(()-> new ResourceNotFoundException("Rol administrador no encontrado")));
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_TEACHER).orElseThrow(()-> new ResourceNotFoundException("Rol docente no encontrado")));
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).orElseThrow(()-> new ResourceNotFoundException("Rol alumno no encontrado")));
        }
        if(usuarioDTO.getRol().equalsIgnoreCase("TEACHER")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_TEACHER).orElseThrow(()-> new ResourceNotFoundException("Rol docente no encontrado")));
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).orElseThrow(()-> new ResourceNotFoundException("Rol alumno no encontrado")));
        }
        if(usuarioDTO.getRol().equalsIgnoreCase("STUDENT")){
            usuarioEntity.getRoles().add(this.rolRepository.findByRolNombre(RolNombre.ROLE_STUDENT).orElseThrow(()-> new ResourceNotFoundException("Rol alumno no encontrado")));
        }

        usuarioEntity.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        apiResponse.setData(this.usuarioRepository.save(usuarioEntity).getUsuarioDTO());
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

    public ApiResponse<JwtDto> refresh(JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        ApiResponse<JwtDto> apiResponse = new ApiResponse<>();
        apiResponse.setData(new JwtDto(token));
        apiResponse.setSuccessful(true);
        apiResponse.setMessage("ok");
        return apiResponse;
    }

}
