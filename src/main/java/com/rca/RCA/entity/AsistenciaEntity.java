package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.AsistenciaDTO;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "Asistencia")
public class AsistenciaEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idattendance", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "state")
    @NotBlank
    private String  state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AlumnoEntity alumnoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clase_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ClaseEntity claseEntity;

    public AsistenciaDTO getAsistenciaDTO(){
        AsistenciaDTO AsistenciaDTO = new AsistenciaDTO();
        AsistenciaDTO.setId(this.getUniqueIdentifier());
        AsistenciaDTO.setCode(this.code);
        AsistenciaDTO.setState(this.state);
        AsistenciaDTO.setAlumnoDTO(this.alumnoEntity.getAlumnoDTO());
        AsistenciaDTO.setClaseDTO(this.claseEntity.getClaseDTO());
        AsistenciaDTO.setStatus(this.getStatus());
        AsistenciaDTO.setCreateAt(this.getCreateAt());
        AsistenciaDTO.setUpdateAt(this.getUpdateAt());
        AsistenciaDTO.setDeleteAt(this.getDeleteAt());
        return AsistenciaDTO;
    }

    public void setAsistenciaDTO(AsistenciaDTO AsistenciaDTO){
        this.setUniqueIdentifier(AsistenciaDTO.getId());
        this.code = AsistenciaDTO.getCode();
        this.state = AsistenciaDTO.getState();
        this.setStatus(AsistenciaDTO.getStatus());
        this.setCreateAt(AsistenciaDTO.getCreateAt());
        this.setUpdateAt(AsistenciaDTO.getUpdateAt());
        this.setDeleteAt(AsistenciaDTO.getDeleteAt());
    }
}
