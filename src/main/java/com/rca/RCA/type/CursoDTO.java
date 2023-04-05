package com.rca.RCA.type;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CursoDTO extends AuditoryDTO{
    private String code;

    @NotBlank (message = "El nombre del  curso no puede estar vacío")
    private String name;
}
