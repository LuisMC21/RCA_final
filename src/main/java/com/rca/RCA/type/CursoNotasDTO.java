package com.rca.RCA.type;


public class CursoNotasDTO {
    private String estudiante;
    private String note;

    public CursoNotasDTO(String estudiante, String note){
        this.estudiante = estudiante;
        this.note = note;
    }

    public String getEstudiante(){
        return estudiante;
    }

    public String getNote(){
        return note;
    }
}
