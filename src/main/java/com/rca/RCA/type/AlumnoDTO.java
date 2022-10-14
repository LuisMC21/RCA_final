package com.rca.RCA.type;

import lombok.Data;

@Data
public class AlumnoDTO extends AuditoryDTO{
    private String code;
    private String enf;
    private String nomcon_pri;
    private String telcon_pri;
    private String nomcon_sec;
    private String telcon_sec;
    private String vacuna;
    private String tip_seg;
    private int usuario_id;
    private int apoderado_id;
}
