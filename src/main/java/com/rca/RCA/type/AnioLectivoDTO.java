package com.rca.RCA.type;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class AnioLectivoDTO extends AuditoryDTO{
    private String code;
    @NotBlank(message = "El nombre del año no puede  estar vacío")
    private String name;
}
