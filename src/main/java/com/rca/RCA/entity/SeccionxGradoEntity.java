package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.SeccionxGradoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "seccionxgrado")
public class SeccionxGradoEntity extends AuditoryEntity {
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
    @OneToMany(mappedBy = "seccionxGradoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<MatriculaEntity> matriculaEntities = new HashSet<>();

    public SeccionxGradoDTO getSeccionxGradoDTO(){
        SeccionxGradoDTO seccionxGradoDTO = new SeccionxGradoDTO();
        seccionxGradoDTO.setId(this.getUniqueIdentifier());
        seccionxGradoDTO.setCode(this.code);
        seccionxGradoDTO.setGradoDTO(this.gradoEntity.getGradoDTO());
        seccionxGradoDTO.setSeccionDTO(this.seccionEntity.getSeccionDTO());
        seccionxGradoDTO.setStatus(this.getStatus());
        seccionxGradoDTO.setCreateAt(this.getCreateAt());
        seccionxGradoDTO.setUpdateAt(this.getUpdateAt());
        seccionxGradoDTO.setDeleteAt(this.getDeleteAt());
        return seccionxGradoDTO;
    }
    public void setSeccionxGradoDTO(SeccionxGradoDTO seccionxGradoDTO){
        this.setUniqueIdentifier(seccionxGradoDTO.getId());
        this.code=seccionxGradoDTO.getCode();
        this.setStatus(seccionxGradoDTO.getStatus());
        this.setCreateAt(seccionxGradoDTO.getCreateAt());
        this.setUpdateAt(seccionxGradoDTO.getUpdateAt());
        this.setDeleteAt(seccionxGradoDTO.getDeleteAt());
    }

}
