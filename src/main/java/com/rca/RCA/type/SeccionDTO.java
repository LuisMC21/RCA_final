package com.rca.RCA.type;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class SeccionDTO extends AuditoryDTO{
    private String code;
    @NotNull (message = "Nombre de la sección no puede estar vacío")
    private Character name;
}
