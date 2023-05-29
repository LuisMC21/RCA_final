package com.rca.RCA.auth.service;

import com.rca.RCA.auth.entity.UsuarioPrincipal;
import com.rca.RCA.entity.UsuarioEntity;
import com.rca.RCA.repository.UsuarioRepository;
import com.rca.RCA.util.ConstantsGeneric;
import com.rca.RCA.util.exceptions.AttributeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombreOrEmail) throws UsernameNotFoundException {
        UsuarioEntity usuario;
        try {
            usuario = usuarioRepository.findByNombreUsuarioOrEmail(nombreOrEmail, ConstantsGeneric.CREATED_STATUS).orElseThrow(()-> new AttributeException("Usuario no existe"));
        } catch (AttributeException e) {
            throw new RuntimeException(e);
        }
        return UsuarioPrincipal.build(usuario);
    }
}