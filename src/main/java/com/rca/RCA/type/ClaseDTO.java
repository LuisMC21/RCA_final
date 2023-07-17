package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ClaseDTO extends AuditoryDTO {
    private String code;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String name;
    private PeriodoDTO periodoDTO;
    private DocentexCursoDTO docentexCursoDTO;
}
