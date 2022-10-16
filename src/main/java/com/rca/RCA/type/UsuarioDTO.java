package com.rca.RCA.type;

import lombok.Data;

@Data
public class UsuarioDTO extends AuditoryDTO{
    private String code;
    private String name;
    private String pa_surname;
    private String ma_surname;
    private String type_doc;
    private String numdoc;
    private String tel;
    private String gra_inst;
    private String email_ins;
    private RolDTO rolDTO;
    private ApoderadoDTO apoderadoDTO;
    private AlumnoDTO alumnoDTO;
    private DocenteDTO docenteDTO;
}
