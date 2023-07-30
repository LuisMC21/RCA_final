package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;import com.rca.RCA.type.MatriculaDTO;
import jakarta.validation.constraints.Past;
import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "matricula")
public class MatriculaEntity extends AuditoryEntity{
    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "code", length = 15)
    private String code;
    //Fecha
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;
    //Sección y grado
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aula_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AulaEntity aulaEntity;
    //Año lectivo
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anio_lectivo_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AnioLectivoEntity anio_lectivoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AlumnoEntity alumnoEntity;

    public MatriculaDTO getMatriculaDTO(){
        MatriculaDTO matriculaDTO = new MatriculaDTO();
        matriculaDTO.setId(this.getUniqueIdentifier());
        matriculaDTO.setCode(this.code);
        matriculaDTO.setDate(this.date);
        matriculaDTO.setAulaDTO(this.aulaEntity.getAulaDTO());
        matriculaDTO.setAnioLectivoDTO(this.anio_lectivoEntity.getAnioLectivoDTO());
        matriculaDTO.setAlumnoDTO(this.alumnoEntity.getAlumnoDTO());
        matriculaDTO.setStatus(this.getStatus());
        matriculaDTO.setCreateAt(this.getCreateAt());
        matriculaDTO.setUpdateAt(this.getUpdateAt());
        matriculaDTO.setDeleteAt(this.getDeleteAt());
        return matriculaDTO;
    }

    public void setMatriculaDTO(MatriculaDTO matriculaDTO){
        this.setUniqueIdentifier(matriculaDTO.getId());
        this.code= matriculaDTO.getCode();
        this.date= matriculaDTO.getDate();
        this.setStatus(matriculaDTO.getStatus());
        this.setCreateAt(matriculaDTO.getCreateAt());
        this.setUpdateAt(matriculaDTO.getUpdateAt());
        this.setDeleteAt(matriculaDTO.getDeleteAt());
    }
}
