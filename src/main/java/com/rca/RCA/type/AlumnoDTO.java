package com.rca.RCA.type;

import lombok.Data;

@Data
public class AlumnoDTO extends AuditoryDTO{
    private String code;
    private String diseases;
    private String namecon_pri;
    private String telcon_pri;
    private String namecon_sec;
    private String telcon_sec;
    private String vaccine;
    private String type_insurance;
    private ApoderadoDTO apoderadoDTO;
    private UsuarioDTO usuarioDTO;
}
