package com.rca.RCA.entity;

import com.rca.RCA.type.AsistenciaDTO;
import lombok.Data;

import javax.persistence.*;

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
    @Column(name = "date")
    private String date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "alumno_id")
    private AlumnoEntity alumnoEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "clase_id")
    private ClaseEntity claseEntity;

    public AsistenciaDTO getAsistenciaDTO(){
        AsistenciaDTO AsistenciaDTO = new AsistenciaDTO();
        AsistenciaDTO.setId(this.getUniqueIdentifier());
        AsistenciaDTO.setCode(this.code);
        AsistenciaDTO.setType(this.date);
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
        this.date = AsistenciaDTO.getType();
        this.setStatus(AsistenciaDTO.getStatus());
        this.setCreateAt(AsistenciaDTO.getCreateAt());
        this.setUpdateAt(AsistenciaDTO.getUpdateAt());
        this.setDeleteAt(AsistenciaDTO.getDeleteAt());
    }
}
