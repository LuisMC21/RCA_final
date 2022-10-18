package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class PeriodoDTO extends AuditoryDTO{
    private String code;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_start;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_end;
    private AnioLectivoDTO anio_lectivoDTO;
}
