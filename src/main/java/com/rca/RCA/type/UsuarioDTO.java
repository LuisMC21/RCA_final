package com.rca.RCA.type;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String code;
    private String appaterno;
    private String apmaterno;
    private String tip_doc;
    private String num_doc;
    private String gra_inst;
    private String correo_ins;
    private String tip_seg;
    private int rol_id;
}
