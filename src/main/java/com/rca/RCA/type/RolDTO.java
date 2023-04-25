package com.rca.RCA.type;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class RolDTO extends AuditoryDTO{

    private String code;
    @NotBlank(message = "Rol name cannot be null")
    private String rolNombre;
}
