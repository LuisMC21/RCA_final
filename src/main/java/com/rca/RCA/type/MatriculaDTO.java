package com.rca.RCA.type;

import lombok.Data;

import java.util.Date;

@Data
public class MatriculaDTO extends AuditoryDTO{
    private String code;
    private Date date;
    private AulaDTO aulaDTO;
    private AnioLectivoDTO anio_lectivoDTO;
    private AlumnoDTO alumnoDTO;
}
