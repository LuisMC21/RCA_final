package com.rca.RCA.util;

import com.rca.RCA.auth.entity.Rol;
import com.rca.RCA.auth.enums.RolNombre;
import com.rca.RCA.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * MUY IMPORTANTE: ESTA CLASE SÓLO SE EJECUTARÁ UNA VEZ PARA CREAR LOS ROLES.
 * UNA VEZ CREADOS SE DEBERÁ ELIMINAR O BIEN COMENTAR EL CÓDIGO
 *
 */
/*
@Component
public class CreateRoles implements CommandLineRunner {

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {
        Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
        Rol rolTeacher = new Rol(RolNombre.ROLE_TEACHER);
        Rol rolStudent = new Rol(RolNombre.ROLE_STUDENT);
        rolService.save(rolAdmin);
        rolService.save(rolTeacher);
        rolService.save(rolStudent);
    }
}
*/
