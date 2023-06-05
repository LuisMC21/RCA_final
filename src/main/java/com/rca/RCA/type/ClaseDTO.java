package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class ClaseDTO extends AuditoryDTO {
    private String code;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;
    private PeriodoDTO periodoDTO;
    private DocentexCursoDTO docentexCursoDTO;
}
