package com.rca.RCA.type;

import lombok.Data;

@Data
public class ImagenFileDTO  extends AuditoryDTO{
    private String name;
    private String imagenBase64;
    private UsuarioDTO usuarioDTO;
}
