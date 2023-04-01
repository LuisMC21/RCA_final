package com.rca.RCA.type;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SeccionDTO extends AuditoryDTO{
    private String code;
    @NotNull (message = "Nombre de la secció no puede estar vacío")
    private Character name;
}
