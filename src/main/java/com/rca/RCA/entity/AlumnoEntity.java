package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.AlumnoDTO;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "alumno")
public class AlumnoEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "diseases")
    @NotBlank
    private String diseases;
    @Column(name = "namecon_pri")
    @NotBlank
    private String namecon_pri;
    @Column(name = "telcon_pri")
    @NotBlank
    private String telcon_pri;
    @Column(name = "namecon_sec")
    @NotBlank
    private String namecon_sec;
    @Column(name = "telcon_sec")
    @NotBlank
    private String telcon_sec;
    @Column(name = "vaccine")
    @NotBlank
    private String vaccine;
    @Column(name = "type_insurance")
    @NotBlank
    private String type_insurance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UsuarioEntity usuarioEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "apoderado_id", referencedColumnName = "id")
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

    public String getName(){
        return this.usuarioEntity.getName();
    }

    public String getNumdoc(){
        return this.usuarioEntity.getNumdoc();
    }

    public String getMa_surname(){
        return this.usuarioEntity.getMa_surname();
    }


    public String getTel() {
        return this.usuarioEntity.getTel();
    }

    public String getPa_surname() {
        return this.usuarioEntity.getPa_surname();
    }
    public String getNombresCompletosAl(){
        return this.usuarioEntity != null ? (this.usuarioEntity.getPa_surname()+" "+this.usuarioEntity.getMa_surname() +" " +this.usuarioEntity.getName()):"----";
    }

    public String getNombreApoderado(){
        return this.apoderadoEntity != null ? (this.apoderadoEntity.getPa_surname() + " " + this.apoderadoEntity.getMa_surname() + " " +this.apoderadoEntity.getName()) :"---";
    }

    public String getTelApoderado(){
        return this.apoderadoEntity != null ? this.apoderadoEntity.getTel() :"---";
    }
    public String getEmailApoderado(){
        return this.apoderadoEntity != null ? this.apoderadoEntity.getEmail():"---";

    }
}
