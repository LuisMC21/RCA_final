package com.rca.RCA.type;

import lombok.Data;

@Data
public class SeccionxGradoDTO extends AuditoryDTO{
    private String code;
    private GradoDTO gradoDTO;
    private SeccionDTO seccionDTO;
}
