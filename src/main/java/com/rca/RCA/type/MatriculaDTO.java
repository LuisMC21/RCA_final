package com.rca.RCA.type;

import lombok.Data;

import java.util.Date;

@Data
public class MatriculaDTO extends AuditoryDTO{
    private String code;
    private Date date;
    private SeccionxGradoDTO seccionxGradoDTO;
    private Anio_LectivoDTO anio_lectivoDTO;
}
