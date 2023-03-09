package com.rca.RCA.type;

import lombok.Data;

@Data
public class ReporteFichaMatriculaDTO {
    private DocenteDTO docenteDTO;
    private CursoDTO cursoDTO;

    public  String getNombreCurso(){
        return this.cursoDTO!=null?this.cursoDTO.getName():"---";
    }
    public  String getNombreDocente(){
        return this.docenteDTO!=null?(this.docenteDTO.getUsuarioDTO().getPa_surname() + " " + this.docenteDTO.getUsuarioDTO().getMa_surname() + " " + this.docenteDTO.getUsuarioDTO().getName()):"---";
    }
}
