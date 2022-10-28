package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MatriculaDTO extends AuditoryDTO{
    private String code;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date date;
    private AulaDTO aulaDTO;
    private AnioLectivoDTO anioLectivoDTO;
    private AlumnoDTO alumnoDTO;
}
