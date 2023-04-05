package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.AulaDTO;
import lombok.Data;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "aula")
public class AulaEntity extends AuditoryEntity {
    //Propiedades
    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "code", length = 15)
    private String code;
    //Grado al que pertenece
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grado_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private GradoEntity gradoEntity;
    //Sección a la que pertenece
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seccion_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private SeccionEntity seccionEntity;

    //Matrículas por grado y sección
    @OneToMany(mappedBy = "aulaEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<MatriculaEntity> matriculaEntities = new HashSet<>();

    @OneToMany(mappedBy = "aulaEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<DocentexCursoEntity> docentexCursoEntities = new HashSet<>();
    public AulaDTO getAulaDTO(){
        AulaDTO aulaDTO = new AulaDTO();
        aulaDTO.setId(this.getUniqueIdentifier());
        aulaDTO.setCode(this.code);
        aulaDTO.setGradoDTO(this.gradoEntity.getGradoDTO());
        aulaDTO.setSeccionDTO(this.seccionEntity.getSeccionDTO());
        aulaDTO.setStatus(this.getStatus());
        aulaDTO.setCreateAt(this.getCreateAt());
        aulaDTO.setUpdateAt(this.getUpdateAt());
        aulaDTO.setDeleteAt(this.getDeleteAt());
        return aulaDTO;
    }
    public void setAulaDTO(AulaDTO aulaDTO){
        this.setUniqueIdentifier(aulaDTO.getId());
        this.code= aulaDTO.getCode();
        this.setStatus(aulaDTO.getStatus());
        this.setCreateAt(aulaDTO.getCreateAt());
        this.setUpdateAt(aulaDTO.getUpdateAt());
        this.setDeleteAt(aulaDTO.getDeleteAt());
    }

}
