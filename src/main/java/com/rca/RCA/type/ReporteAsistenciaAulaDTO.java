package com.rca.RCA.type;

import lombok.Data;

@Data
public class ReporteAsistenciaAulaDTO {

    private String alumno;

    private Integer asistencias;

    private Integer faltas;

    private Integer justificadas;
}
