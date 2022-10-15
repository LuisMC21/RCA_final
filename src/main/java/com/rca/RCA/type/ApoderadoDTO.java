package com.rca.RCA.type;

import lombok.Data;

@Data
public class ApoderadoDTO extends AuditoryDTO{
    private String code;
    private String email;
    private UsuarioDTO usuarioDTO;
}
