package com.rca.RCA.type;


import lombok.Data;

@Data
public class NoticiaFileDTO {
    private String title;
    private String sommelier;
    private String descrip;
    private String imagenBase64;
    private String date;
    private UsuarioDTO usuarioDTO;
}
