package com.rca.RCA.type;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class DocenteDTO extends AuditoryDTO{
    private String code;
    private String experience;
    private Character dose;
    private String specialty;
    @Valid
    private UsuarioDTO usuarioDTO;
}
