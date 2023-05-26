package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class NoticiaDTO extends AuditoryDTO{
    private String code;
    private String title;
    private String sommelier;
    private String descrip;
    private String route;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private UsuarioDTO usuarioDTO;
}
