package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.AlumnoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Alumno")
public class AlumnoEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "diseases")
    private String diseases;
    @Column(name = "namecon_pri")
    private String namecon_pri;
    @Column(name = "telcon_pri")
    private String telcon_pri;
    @Column(name = "namecon_sec")
    private String namecon_sec;
    @Column(name = "telcon_sec")
    private String telcon_sec;
    @Column(name = "vaccine")
    private String vaccine;
    @Column(name = "type_insurance")
    private String type_insurance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UsuarioEntity usuarioEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apoderado_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ApoderadoEntity apoderadoEntity;

    @OneToMany(mappedBy = "alumnoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<AsistenciaEntity> asistenciaEntities = new HashSet<>();

    @OneToMany(mappedBy = "alumnoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<EvaluacionEntity> evaluacionEntities = new HashSet<>();

    @OneToMany(mappedBy = "alumnoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<MatriculaEntity> matriculaEntities = new HashSet<>();

    public AlumnoDTO getAlumnoDTO(){
        AlumnoDTO AlumnoDTO = new AlumnoDTO();
        AlumnoDTO.setId(this.getUniqueIdentifier());
        AlumnoDTO.setCode(this.code);
        AlumnoDTO.setDiseases(this.diseases);
        AlumnoDTO.setNamecon_pri(this.namecon_pri);
        AlumnoDTO.setTelcon_pri(this.telcon_pri);
        AlumnoDTO.setNamecon_sec(this.namecon_sec);
        AlumnoDTO.setTelcon_sec(this.telcon_sec);
        AlumnoDTO.setVaccine(this.vaccine);
        AlumnoDTO.setType_insurance(this.type_insurance);
        AlumnoDTO.setUsuarioDTO(this.usuarioEntity.getUsuarioDTO());
        AlumnoDTO.setApoderadoDTO(this.apoderadoEntity.getApoderadoDTO());
        AlumnoDTO.setStatus(this.getStatus());
        AlumnoDTO.setCreateAt(this.getCreateAt());
        AlumnoDTO.setUpdateAt(this.getUpdateAt());
        AlumnoDTO.setDeleteAt(this.getDeleteAt());
        return AlumnoDTO;
    }

    public void setAlumnoDTO(AlumnoDTO AlumnoDTO){
        this.setUniqueIdentifier(AlumnoDTO.getId());
        this.code= AlumnoDTO.getCode();
        this.diseases= AlumnoDTO.getDiseases();
        this.namecon_pri = AlumnoDTO.getNamecon_pri();
        this.telcon_pri = AlumnoDTO.getTelcon_pri();
        this.namecon_sec = AlumnoDTO.getNamecon_sec();
        this.telcon_sec = AlumnoDTO.getTelcon_sec();
        this.vaccine = AlumnoDTO.getVaccine();
        this.type_insurance = AlumnoDTO.getType_insurance();
        this.setStatus(AlumnoDTO.getStatus());
        this.setCreateAt(AlumnoDTO.getCreateAt());
        this.setUpdateAt(AlumnoDTO.getUpdateAt());
        this.setDeleteAt(AlumnoDTO.getDeleteAt());
    }
}
