package com.rca.RCA.type;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RolDTO extends AuditoryDTO{

    private String code;
    @NotBlank(message = "Rol name cannot be null")
    private String name;
}
