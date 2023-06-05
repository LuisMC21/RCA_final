package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ClaseDTO extends AuditoryDTO {
    private String code;
    @NotNull(message = "Fecha de nacimiento no puede estar vacía")
    @JsonFormat(pattern = "yyyy-MM-dd") @Past
    private LocalDate date;
    private String name;
    private PeriodoDTO periodoDTO;
    private DocentexCursoDTO docentexCursoDTO;
}
