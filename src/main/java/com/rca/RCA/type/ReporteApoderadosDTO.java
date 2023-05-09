package com.rca.RCA.type;

import lombok.Data;

@Data
public class ReporteApoderadosDTO {

    private AlumnoDTO alumnoDTO;
    private ApoderadoDTO apoderadoDTO;


    public String getNombresCompletosAl(){
        return this.alumnoDTO !=null ? (this.alumnoDTO.getUsuarioDTO().getPa_surname() + " " + this.alumnoDTO.getUsuarioDTO().getMa_surname() + " " + this.alumnoDTO.getUsuarioDTO().getName()): "---";
    }
    public String getNombreApoderado(){
        return this.apoderadoDTO!=null ?(this.apoderadoDTO.getPa_surname() + " " + this.apoderadoDTO.getMa_surname() + " " + this.apoderadoDTO.getName()): "---";
    }
    public String getTelApoderado(){
        return this.apoderadoDTO!=null?this.apoderadoDTO.getTel():"---";
    }
    public String getEmailApoderado(){
        return this.apoderadoDTO!=null?this.apoderadoDTO.getEmail():"---";
    }
}
