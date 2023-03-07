package com.rca.RCA.type;

import lombok.Data;

@Data
public class ReporteApoderadosDTO {

    private AlumnoDTO alumnoDTO;
    private ApoderadoDTO apoderadoDTO;


    public String getNombresCompletosAl(){
        return (this.alumnoDTO.getUsuarioDTO().getPa_surname() + " " + this.alumnoDTO.getUsuarioDTO().getMa_surname() + " " + this.alumnoDTO.getUsuarioDTO().getName());
    }
    public String getNombreApoderado(){
        return (this.apoderadoDTO.getUsuarioDTO().getPa_surname() + " " + this.apoderadoDTO.getUsuarioDTO().getMa_surname() + " " + this.apoderadoDTO.getUsuarioDTO().getName());
    }
    public String getTelApoderado(){
        return this.apoderadoDTO.getUsuarioDTO().getTel();
    }
    public String getEmailApoderado(){
        return this.apoderadoDTO.getEmail();
    }
}
