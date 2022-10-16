package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.PeriodoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "periodo")
public class PeriodoEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "code", length = 15)
    private String code;
    //Fecha inicial
    @JsonFormat(pattern = "YYYY-MM-dd")
    @Column(name = "date_start")
    private Date date_start;

    //Fecha final
    @JsonFormat(pattern = "YYYY-MM-dd")
    @Column(name = "date_end")
    private Date date_end;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anio_lectivo_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Anio_LectivoEntity anio_lectivoEntity;

    @OneToMany(mappedBy = "periodoEntity", cascade=CascadeType.ALL)
    private Set<ClaseEntity> claseEntities= new HashSet<ClaseEntity>();

    public PeriodoDTO getPeriodoDTO(){
        PeriodoDTO periodoDTO = new PeriodoDTO();
        periodoDTO.setId(this.getUniqueIdentifier());
        periodoDTO.setCode(this.code);
        periodoDTO.setDate_start(this.date_start);
        periodoDTO.setDate_end(this.date_end);
        periodoDTO.setAnio_lectivoDTO(this.anio_lectivoEntity.getAnio_LectivoDTO());
        periodoDTO.setStatus(this.getStatus());
        periodoDTO.setCreateAt(this.getCreateAt());
        periodoDTO.setUpdateAt(this.getUpdateAt());
        periodoDTO.setDeleteAt(this.getDeleteAt());
        return periodoDTO;
    }
    public void setPeriodoDTO(PeriodoDTO periodoDTO){
        this.setUniqueIdentifier(periodoDTO.getId());
        this.code=periodoDTO.getCode();
        this.date_start=periodoDTO.getDate_start();
        this.date_end=periodoDTO.getDate_end();
        this.setStatus(periodoDTO.getStatus());
        this.setCreateAt(periodoDTO.getCreateAt());
        this.setUpdateAt(periodoDTO.getUpdateAt());
        this.setDeleteAt(periodoDTO.getDeleteAt());
    }

}
