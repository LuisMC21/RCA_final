package com.rca.RCA.type;

import lombok.Data;

@Data
public class ImagenDTO extends AuditoryDTO{
    private String code;
    private String name;
    private String route;
    private UsuarioDTO usuarioDTO;
}
