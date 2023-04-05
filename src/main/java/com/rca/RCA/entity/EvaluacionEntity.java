package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.EvaluacionDTO;
import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "evaluacion")
public class EvaluacionEntity extends AuditoryEntity{
    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "code", length = 15)
    private String code;
    //Fecha
    @JsonFormat(pattern = "YYYY-MM-dd")
    @Column(name = "date")
    private Date date;
    //Calificación (AD,A,B,C)
    @Column(name = "note", length = 3)
    private String note;
    //Periodo
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "periodo_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PeriodoEntity periodoEntity;
    //Docente y curso de la evaluación
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docentexcurso_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DocentexCursoEntity docentexCursoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AlumnoEntity alumnoEntity;

    public EvaluacionDTO getEvaluacionDTO(){
        EvaluacionDTO evaluacionDTO = new EvaluacionDTO();
        evaluacionDTO.setId(this.getUniqueIdentifier());
        evaluacionDTO.setCode(this.code);
        evaluacionDTO.setDate(this.date);
        evaluacionDTO.setNote(this.note);
        evaluacionDTO.setPeriodoDTO(this.periodoEntity.getPeriodoDTO());
        evaluacionDTO.setDocentexCursoDTO(this.docentexCursoEntity.getDocentexCursoDTO());
        evaluacionDTO.setAlumnoDTO(this.alumnoEntity.getAlumnoDTO());
        evaluacionDTO.setStatus(this.getStatus());
        evaluacionDTO.setCreateAt(this.getCreateAt());
        evaluacionDTO.setUpdateAt(this.getUpdateAt());
        evaluacionDTO.setDeleteAt(this.getDeleteAt());
        return evaluacionDTO;
    }

    public void setEvaluacionDTO(EvaluacionDTO evaluacionDTO){
        this.setUniqueIdentifier(evaluacionDTO.getId());
        this.code=evaluacionDTO.getCode();
        this.date=evaluacionDTO.getDate();
        this.note=evaluacionDTO.getNote();
        this.setStatus(evaluacionDTO.getStatus());
        this.setCreateAt(evaluacionDTO.getCreateAt());
        this.setUpdateAt(evaluacionDTO.getUpdateAt());
        this.setDeleteAt(evaluacionDTO.getDeleteAt());
    }
}
