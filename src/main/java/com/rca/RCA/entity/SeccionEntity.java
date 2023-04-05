package com.rca.RCA.entity;

import com.rca.RCA.type.SeccionDTO;
import lombok.Data;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "seccion")
public class SeccionEntity extends AuditoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "name")
    private Character name;

    @OneToMany(mappedBy = "seccionEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<AulaEntity> aulaEntities = new HashSet<>();

    public SeccionDTO getSeccionDTO(){
        SeccionDTO seccionDTO = new SeccionDTO();
        seccionDTO.setId(this.getUniqueIdentifier());
        seccionDTO.setCode(this.code);
        seccionDTO.setName(this.name);
        seccionDTO.setStatus(this.getStatus());
        seccionDTO.setCreateAt(this.getCreateAt());
        seccionDTO.setUpdateAt(this.getUpdateAt());
        seccionDTO.setDeleteAt(this.getDeleteAt());
        return seccionDTO;
    }

    public void setSeccionDTO(SeccionDTO seccionDTO){
        this.setUniqueIdentifier(seccionDTO.getId());
        this.code= seccionDTO.getCode();
        this.name= seccionDTO.getName();
        this.setStatus(seccionDTO.getStatus());
        this.setCreateAt(seccionDTO.getCreateAt());
        this.setUpdateAt(seccionDTO.getUpdateAt());
        this.setDeleteAt(seccionDTO.getDeleteAt());
    }
}
