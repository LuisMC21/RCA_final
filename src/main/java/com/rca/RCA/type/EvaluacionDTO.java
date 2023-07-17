package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EvaluacionDTO extends AuditoryDTO{
    private String code;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;
    private String note;
    private PeriodoDTO periodoDTO;
    private DocentexCursoDTO docentexCursoDTO;
    private AlumnoDTO alumnoDTO;
}
