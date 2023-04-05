package com.rca.RCA.entity;

import com.rca.RCA.type.GradoDTO;
import lombok.Data;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "grado")
public class GradoEntity extends AuditoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "name")
    private Character name;

    @OneToMany(mappedBy = "gradoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<AulaEntity> aulaEntities = new HashSet<>();

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
