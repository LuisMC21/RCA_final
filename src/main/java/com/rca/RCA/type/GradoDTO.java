package com.rca.RCA.type;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class GradoDTO extends AuditoryDTO{
    private String code;
    @NotBlank(message = "El nombre del grado no puede estar vacío")
    private Character name;
}