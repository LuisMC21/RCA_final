package com.rca.RCA.type;

import lombok.Data;

@Data
public class DocenteDTO extends AuditoryDTO{
    private String code;
    private String experience;
    private Character dose;
    private String specialty;
    private UsuarioDTO usuarioDTO;
}
