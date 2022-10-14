package com.rca.RCA.type;

import lombok.Data;

import java.util.Date;

@Data
public class EvaluacionDTO extends AuditoryDTO{
    private String code;
    private Date date;
    private String note;
    private PeriodoDTO periodoDTO;
    private DocentexCursoDTO docentexCursoDTO;
}
