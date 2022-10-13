package com.rca.RCA.entity;

import com.rca.RCA.type.SeccionDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Seccion")
public class SeccionEntity extends AuditoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "cod", length = 40)
    private String code;
    @Column(name = "nom")
    private Character name;

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
