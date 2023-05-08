package com.rca.RCA.type;

import lombok.Data;

import java.util.List;

@Data
public class ReporteAsistenciaAulaDTO {

    private String alumno;

    private Integer presente;

    private Integer ausente;

    private Integer justificadas;
}
