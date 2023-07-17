package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.DocenteDTO;
import com.rca.RCA.type.DocentexCursoDTO;
import lombok.Data;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "docentexcurso")
public class DocentexCursoEntity extends AuditoryEntity{
    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "code", length = 15)
    private String code;
    //Docente al que le pertenece
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docente_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DocenteEntity docenteEntity;
    //Curso al que le pertenece
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CursoEntity cursoEntity;

    //D
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aula_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AulaEntity aulaEntity;

    //Evaluaciones del docente
    @OneToMany(mappedBy = "docentexCursoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<EvaluacionEntity> evaluacionEntities = new HashSet<>();
    //Asignaturas del aula
    @OneToMany(mappedBy = "docentexCursoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<ClaseEntity> claseEntities = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anio_lectivo_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AnioLectivoEntity anio_lectivoEntity;

    public DocentexCursoDTO getDocentexCursoDTO(){
        DocentexCursoDTO docentexCursoDTO = new DocentexCursoDTO();
        docentexCursoDTO.setId(this.getUniqueIdentifier());
        docentexCursoDTO.setCode(this.code);
        docentexCursoDTO.setDocenteDTO(this.docenteEntity.getDocenteDTO());
        docentexCursoDTO.setCursoDTO(this.cursoEntity.getCursoDTO());
        docentexCursoDTO.setAulaDTO(this.aulaEntity.getAulaDTO());
        docentexCursoDTO.setAnioLectivoDTO(this.anio_lectivoEntity.getAnioLectivoDTO());
        docentexCursoDTO.setStatus(this.getStatus());
        docentexCursoDTO.setCreateAt(this.getCreateAt());
        docentexCursoDTO.setUpdateAt(this.getUpdateAt());
        docentexCursoDTO.setDeleteAt(this.getDeleteAt());
        return docentexCursoDTO;
    }

    public void setDocenteDTO(DocenteDTO docenteDTO){
        this.setUniqueIdentifier(docenteDTO.getId());
        this.code= docenteDTO.getCode();
        this.setStatus(docenteDTO.getStatus());
        this.setCreateAt(docenteDTO.getCreateAt());
        this.setUpdateAt(docenteDTO.getUpdateAt());
        this.setDeleteAt(docenteDTO.getDeleteAt());
    }
}
