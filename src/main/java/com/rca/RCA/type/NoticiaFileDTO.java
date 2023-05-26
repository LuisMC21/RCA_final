package com.rca.RCA.type;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class NoticiaFileDTO extends AuditoryDTO{
    private String code;
    private String title;
    private String sommelier;
    private String descrip;
    private String imagenBase64;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private UsuarioDTO usuarioDTO;
}
