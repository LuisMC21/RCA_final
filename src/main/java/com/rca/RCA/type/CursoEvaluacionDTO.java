package com.rca.RCA.type;


public class CursoEvaluacionDTO{
    private String name;
    private String note;

    public CursoEvaluacionDTO(String name, String note){
        this.name = name;
        this.note = note;
    }

    public String getName(){
        return name;
    }

    public String getNote(){
        return note;
    }
}
