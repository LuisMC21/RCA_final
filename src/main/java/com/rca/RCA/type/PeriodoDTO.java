package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class PeriodoDTO extends AuditoryDTO{
    private String code;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date date_start;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date date_end;
    private Anio_LectivoDTO anio_lectivoDTO;
}
