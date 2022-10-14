package com.rca.RCA.entity;

import com.rca.RCA.type.CursoDTO;
import com.rca.RCA.type.DocenteDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "docente")
public class DocenteEntity extends AuditoryEntity{
    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "cod", length = 15)
    private String code;
    //Años de experiencia
    @Column(name = "exp", length = 40)
    private String experience;
    //Dosis de vacuna COVID
    @Column(name = "vac")
    private Character dose;
    //Especialidad
    @Column(name = "espec")
    private String specialty;
    //Cursos del docente
    @OneToMany(mappedBy = "docenteEntity", cascade=CascadeType.ALL)
    private Set<DocentexCursoEntity> docentexCursoEntities = new HashSet<>();

    public DocenteDTO getDocenteDTO(){
        DocenteDTO docenteDTO = new DocenteDTO();
        docenteDTO.setId(this.getUniqueIdentifier());
        docenteDTO.setCode(this.code);
        docenteDTO.setExperience(this.experience);
        docenteDTO.setDose(this.dose);
        docenteDTO.setSpecialty(this.specialty);
        docenteDTO.setStatus(this.getStatus());
        docenteDTO.setCreateAt(this.getCreateAt());
        docenteDTO.setUpdateAt(this.getUpdateAt());
        docenteDTO.setDeleteAt(this.getDeleteAt());
        return docenteDTO;
    }

    public void setDocenteDTO(DocenteDTO docenteDTO){
        this.setUniqueIdentifier(docenteDTO.getId());
        this.code= docenteDTO.getCode();
        this.experience= docenteDTO.getExperience();
        this.dose= docenteDTO.getDose();
        this.specialty= docenteDTO.getSpecialty();
        this.setStatus(docenteDTO.getStatus());
        this.setCreateAt(docenteDTO.getCreateAt());
        this.setUpdateAt(docenteDTO.getUpdateAt());
        this.setDeleteAt(docenteDTO.getDeleteAt());
    }
}
