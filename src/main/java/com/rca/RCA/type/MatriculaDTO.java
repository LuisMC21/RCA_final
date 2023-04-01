package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MatriculaDTO extends AuditoryDTO{
    private String code;
    @JsonFormat(pattern = "YYYY-MM-dd")
    @NotNull (message = "La fecha de matrícula no puede estar vacía")
    private Date date;
    private AulaDTO aulaDTO;
    private AnioLectivoDTO anioLectivoDTO;
    private AlumnoDTO alumnoDTO;
}
