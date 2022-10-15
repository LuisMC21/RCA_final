package com.rca.RCA.type;

import lombok.Data;

@Data
public class NoticiaDTO extends AuditoryDTO{
    private String code;
    private String title;
    private String sommelier;
    private String descrip;
    private String image;
    private String date;
    private UsuarioDTO usuarioDTO;
}
