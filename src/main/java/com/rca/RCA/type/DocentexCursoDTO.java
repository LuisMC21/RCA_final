package com.rca.RCA.type;

import lombok.Data;

@Data
public class DocentexCursoDTO extends AuditoryDTO{
    private String code;
    private DocenteDTO docenteDTO;
    private CursoDTO cursoDTO;
    private AulaDTO aulaDTO;
    private AnioLectivoDTO anioLectivoDTO;
}
