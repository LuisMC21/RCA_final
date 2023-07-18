package com.rca.RCA.type;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AlumnoDTO extends AuditoryDTO{
    private String code;
    @NotBlank
    private String diseases;
    @NotBlank
    private String namecon_pri;
    @NotBlank
    private String telcon_pri;
    @NotBlank
    private String namecon_sec;
    @NotBlank
    private String telcon_sec;
    @NotBlank
    private String vaccine;
    @NotBlank
    private String type_insurance;
    private ApoderadoDTO apoderadoDTO;
    private UsuarioDTO usuarioDTO;
}
