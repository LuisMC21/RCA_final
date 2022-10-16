package com.rca.RCA.type;

import lombok.Data;

@Data
public class AsistenciaDTO extends AuditoryDTO{
    private String code;
    private String type;
    private AlumnoDTO alumnoDTO;
    private ClaseDTO claseDTO;
}
