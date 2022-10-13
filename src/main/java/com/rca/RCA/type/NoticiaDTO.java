package com.rca.RCA.type;

import lombok.Data;

@Data
public class NoticiaDTO {
    private String code;
    private String titulo;
    private String sum;
    private String descrip;
    private String imagen;
    private String fecha;
    private int usuario_id;
}
