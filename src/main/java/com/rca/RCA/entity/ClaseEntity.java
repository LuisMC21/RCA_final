package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.ClaseDTO;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Clase")
public class ClaseEntity extends AuditoryEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @JsonFormat(pattern = "YYYY-MM-dd")
    @Column(name = "date")
    private Date date;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docentexcurso_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DocentexCursoEntity docentexCursoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "periodo_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PeriodoEntity periodoEntity;


    @OneToMany(mappedBy = "claseEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<AsistenciaEntity> asistenciaEntities = new HashSet<>();
    public ClaseDTO getClaseDTO(){
        ClaseDTO ClaseDTO = new ClaseDTO();
        ClaseDTO.setId(this.getUniqueIdentifier());
        ClaseDTO.setCode(this.code);
        ClaseDTO.setDate(this.date);
        ClaseDTO.setName(this.name);
        ClaseDTO.setPeriodoDTO(this.periodoEntity.getPeriodoDTO());
        ClaseDTO.setDocentexCursoDTO(this.docentexCursoEntity.getDocentexCursoDTO());
        ClaseDTO.setStatus(this.getStatus());
        ClaseDTO.setCreateAt(this.getCreateAt());
        ClaseDTO.setUpdateAt(this.getUpdateAt());
        ClaseDTO.setDeleteAt(this.getDeleteAt());
        return ClaseDTO;
    }

    public void setClaseDTO(ClaseDTO ClaseDTO){
        this.setUniqueIdentifier(ClaseDTO.getId());
        this.code= ClaseDTO.getCode();
        this.date= ClaseDTO.getDate();
        this.name= ClaseDTO.getName();
        this.setStatus(ClaseDTO.getStatus());
        this.setCreateAt(ClaseDTO.getCreateAt());
        this.setUpdateAt(ClaseDTO.getUpdateAt());
        this.setDeleteAt(ClaseDTO.getDeleteAt());
    }

    public String getFechaFormatString(){
        return this.date != null ?
                new SimpleDateFormat("dd-MM-yyyy").format(this.date) :
                "---";
    }
}
