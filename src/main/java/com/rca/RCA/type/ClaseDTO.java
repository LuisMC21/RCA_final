package com.rca.RCA.type;

import lombok.Data;

import java.util.Date;

@Data
public class ClaseDTO extends AuditoryDTO {
    private String code;
    private Date date;
    private PeriodoDTO periodoDTO;
    private DocentexCursoDTO docentexCursoDTO;
}
