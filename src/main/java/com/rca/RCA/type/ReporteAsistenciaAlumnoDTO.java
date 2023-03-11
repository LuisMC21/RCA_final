package com.rca.RCA.type;

import lombok.Data;

@Data
public class ReporteAsistenciaAlumnoDTO {
    private ClaseDTO claseDTO;
    private AsistenciaDTO asistenciaDTO;
    private  CursoDTO cursoDTO;

    public String getFechaAsistencia(){
        return this.claseDTO!=null?this.claseDTO.getDate().toString():"---";
    }
    public  String getNombreCurso(){
        return this.cursoDTO!=null?this.cursoDTO.getName():"---";
    }
    public String getEstado(){
        return this.asistenciaDTO!=null?this.asistenciaDTO.getState():"---";
    }
}
