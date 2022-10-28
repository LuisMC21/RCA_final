package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.ClaseDTO;
import lombok.Data;

import javax.persistence.*;

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
    @Column(name = "date")
    private String date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docentexcurso_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DocentexCursoEntity docentexCursoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "periodo_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PeriodoEntity periodoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aula_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AulaEntity aulaEntity;

    public ClaseDTO getClaseDTO(){
        ClaseDTO ClaseDTO = new ClaseDTO();
        ClaseDTO.setId(this.getUniqueIdentifier());
        ClaseDTO.setCode(this.code);
        ClaseDTO.setDate(this.date);
        ClaseDTO.setPeriodoDTO(this.periodoEntity.getPeriodoDTO());
        ClaseDTO.setAulaDTO(this.aulaEntity.getAulaDTO());
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
        this.setStatus(ClaseDTO.getStatus());
        this.setCreateAt(ClaseDTO.getCreateAt());
        this.setUpdateAt(ClaseDTO.getUpdateAt());
        this.setDeleteAt(ClaseDTO.getDeleteAt());
    }
}
