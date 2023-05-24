package com.rca.RCA.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginUsuario {
    @NotBlank(message = "Nombre de usuario vacío")
    private String nombreUsuario;
    @NotBlank(message = "Contraseña vacía")
    private String password;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
