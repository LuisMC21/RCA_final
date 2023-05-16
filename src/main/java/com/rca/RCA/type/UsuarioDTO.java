package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.util.Date;

@Data
public class UsuarioDTO extends AuditoryDTO{
    private String code;
    @NotBlank (message = "Nombre de usuario no puede estar vacío")
    private String nombreUsuario;
    @NotBlank (message = "Nombre no puede estar vacío")
    private String name;
    @NotBlank (message = "Apellido paterno de usuario no puede estar vacío")
    private String pa_surname;
    @NotBlank (message = "Apellido materno de usuario no puede estar vacío")
    private String ma_surname;
    @NotNull (message = "Fecha de nacimiento no puede estar vacía")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    @NotBlank (message = "Tipo de documento no puede estar vacío")
    private String type_doc;
    @NotBlank (message = "Número de documento no puede estar vacío")
    private String numdoc;
    private String tel;
    private String gra_inst;
    @NotBlank(message = "Email no puede estar vacío")
    private String email;
    private String password;
    @NotBlank(message = "Rol no puede estar vacío")
    private String rol;
}
