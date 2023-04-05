package com.rca.RCA.auth.service;

import com.rca.RCA.auth.entity.Usuario;
import com.rca.RCA.auth.entity.UsuarioPrincipal;
import com.rca.RCA.auth.repository.UsuarioRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioRepository2 usuarioRepository2;

    @Override
    public UserDetails loadUserByUsername(String nombreOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository2.findByNombreUsuarioOrEmail(nombreOrEmail, nombreOrEmail).get();
        return UsuarioPrincipal.build(usuario);
    }
}