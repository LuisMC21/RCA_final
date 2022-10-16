package com.rca.RCA.entity;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "docentexcurso_id")
    private DocentexCursoEntity docentexCursoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "periodo_id")
    private PeriodoEntity periodoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "seccionxgrado_id")
    private SeccionxGradoEntity seccionxGradoEntity;

    public ClaseDTO getClaseDTO(){
        ClaseDTO ClaseDTO = new ClaseDTO();
        ClaseDTO.setId(this.getUniqueIdentifier());
        ClaseDTO.setCode(this.code);
        ClaseDTO.setDate(this.date);
        ClaseDTO.setPeriodoDTO(this.periodoEntity.getPeriodoDTO());
        ClaseDTO.setSeccionxGradoDTO(this.seccionxGradoEntity.getSeccionxGradoDTO());
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
