package com.rca.RCA.entity;

import com.rca.RCA.type.DocenteDTO;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @Column(name = "code", length = 15)
    private String code;
    //Años de experiencia
    @Column(name = "experience", length = 40)
    @NotBlank(message = "Necesita agregar la experiencia")
    private String experience;
    //Dosis de vacuna COVID
    @Column(name = "dose")
    private Character dose;
    //Especialidad
    @Column(name = "specialty")
    private String specialty;
    //Cursos del docente
    @OneToMany(mappedBy = "docenteEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<DocentexCursoEntity> docentexCursoEntities = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UsuarioEntity usuarioEntity;

    public DocenteDTO getDocenteDTO(){
        DocenteDTO docenteDTO = new DocenteDTO();
        docenteDTO.setId(this.getUniqueIdentifier());
        docenteDTO.setCode(this.code);
        docenteDTO.setExperience(this.experience);
        docenteDTO.setDose(this.dose);
        docenteDTO.setSpecialty(this.specialty);
        docenteDTO.setUsuarioDTO(this.usuarioEntity.getUsuarioDTO());
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
