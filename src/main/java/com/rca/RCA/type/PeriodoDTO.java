package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
@Data
public class PeriodoDTO extends AuditoryDTO{
    private String code;
    @NotBlank (message = "El nombre del periodo no puede estar vacío")
    private String name;
    @NotNull(message = "Fecha de inicio requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_start;
    @NotNull(message = "Fecha de fin requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_end;
    private AnioLectivoDTO anio_lectivoDTO;
}
