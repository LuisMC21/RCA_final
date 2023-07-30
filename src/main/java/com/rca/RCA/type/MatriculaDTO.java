package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
public class MatriculaDTO extends AuditoryDTO{
    private String code;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull (message = "La fecha de matrícula no puede estar vacía")
    private LocalDate date;
    private AulaDTO aulaDTO;
    private AnioLectivoDTO anioLectivoDTO;
    private AlumnoDTO alumnoDTO;
}
