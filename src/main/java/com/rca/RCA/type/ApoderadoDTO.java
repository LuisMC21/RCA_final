package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ApoderadoDTO extends AuditoryDTO{
    private String code;
    @NotBlank(message = "Noombre de apoderado no puede estar vacío")
    private String name;
    @NotBlank(message = "Apellido paterno de apoderado no puede estar vacío")
    private String pa_surname;
    @NotBlank (message = "Apellido materno de apoderado no puede estar vacío")
    private String ma_surname;
    @NotNull(message = "Fecha de nacimiento no puede estar vacía")
    @JsonFormat(pattern = "yyyy-MM-dd") @Past
    private LocalDate birthdate;
    @NotBlank (message = "Tipo de documento no puede estar vacío")
    private String type_doc;
    @NotBlank (message = "Número de documento no puede estar vacío")
    private String numdoc;
    @NotBlank (message = "Email no puede estar vacío")
    private String email;
    @NotBlank (message = "Teléfono no puede estar vacío")
    private String tel;
}
