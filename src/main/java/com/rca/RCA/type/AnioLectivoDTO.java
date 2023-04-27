package com.rca.RCA.type;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AnioLectivoDTO extends AuditoryDTO{
    private String code;
    @NotBlank(message = "El nombre del año no puede  estar vacío")
    private String name;
}
