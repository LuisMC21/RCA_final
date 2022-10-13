package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rca.RCA.type.GradoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Grado")
public class GradoEntity extends AuditoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "cod", length = 40)
    private String code;
    @Column(name = "nom")
    private Character name;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinTable(name = "seccionxgrado",
            joinColumns = {@JoinColumn(name = "grado_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "seccion_id", referencedColumnName = "id")}
    )
    private List<SeccionEntity> secciones = new ArrayList<>();

    public GradoDTO getGradoDTO(){
        GradoDTO gradoDTO = new GradoDTO();
        gradoDTO.setId(this.getUniqueIdentifier());
        gradoDTO.setCode(this.code);
        gradoDTO.setName(this.name);
        gradoDTO.setStatus(this.getStatus());
        gradoDTO.setCreateAt(this.getCreateAt());
        gradoDTO.setUpdateAt(this.getUpdateAt());
        gradoDTO.setDeleteAt(this.getDeleteAt());
        return gradoDTO;
    }

    public void setGradoDTO(GradoDTO gradoDTO){
        this.setUniqueIdentifier(gradoDTO.getId());
        this.code= gradoDTO.getCode();
        this.name= gradoDTO.getName();
        this.setStatus(gradoDTO.getStatus());
        this.setCreateAt(gradoDTO.getCreateAt());
        this.setUpdateAt(gradoDTO.getUpdateAt());
        this.setDeleteAt(gradoDTO.getDeleteAt());
    }
}
