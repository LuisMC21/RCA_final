package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rca.RCA.type.RolDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Rol")
public class RolEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "rolEntity", cascade=CascadeType.ALL)
    private Set<UsuarioEntity> usuarioEntities = new HashSet<>();

    public RolDTO getRolDTO(){
        RolDTO RolDTO = new RolDTO();
        RolDTO.setId(this.getUniqueIdentifier());
        RolDTO.setCode(this.code);
        RolDTO.setName(this.name);
        RolDTO.setStatus(this.getStatus());
        RolDTO.setCreateAt(this.getCreateAt());
        RolDTO.setUpdateAt(this.getUpdateAt());
        RolDTO.setDeleteAt(this.getDeleteAt());
        return RolDTO;
    }

    public void setRolDTO(RolDTO RolDTO){
        this.setUniqueIdentifier(RolDTO.getId());
        this.code= RolDTO.getCode();
        this.name= RolDTO.getName();
        this.setStatus(RolDTO.getStatus());
        this.setCreateAt(RolDTO.getCreateAt());
        this.setUpdateAt(RolDTO.getUpdateAt());
        this.setDeleteAt(RolDTO.getDeleteAt());
    }
}
