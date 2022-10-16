package com.rca.RCA.type;

import lombok.Data;

@Data
public class ClaseDTO extends AuditoryDTO {
    private String code;
    private String date;
    private PeriodoDTO periodoDTO;
    private DocentexCursoDTO docentexCursoDTO;
    private SeccionxGradoDTO seccionxGradoDTO;
}
