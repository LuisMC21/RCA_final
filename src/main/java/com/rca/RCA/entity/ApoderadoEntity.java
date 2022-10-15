package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rca.RCA.type.ApoderadoDTO;
import com.rca.RCA.type.ApoderadoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Apoderado")
public class ApoderadoEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idapoderado", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    private UsuarioEntity usuarioEntity;

    public ApoderadoDTO getApoderadoDTO(){
        ApoderadoDTO ApoderadoDTO = new ApoderadoDTO();
        ApoderadoDTO.setId(this.getUniqueIdentifier());
        ApoderadoDTO.setCode(this.code);
        ApoderadoDTO.setEmail(this.email);
        ApoderadoDTO.setUsuarioDTO(this.usuarioEntity.getUsuarioDTO());
        ApoderadoDTO.setStatus(this.getStatus());
        ApoderadoDTO.setCreateAt(this.getCreateAt());
        ApoderadoDTO.setUpdateAt(this.getUpdateAt());
        ApoderadoDTO.setDeleteAt(this.getDeleteAt());
        return ApoderadoDTO;
    }

    public void setApoderadoDTO(ApoderadoDTO ApoderadoDTO){
        this.setUniqueIdentifier(ApoderadoDTO.getId());
        this.code = ApoderadoDTO.getCode();
        this.email = ApoderadoDTO.getEmail();
        this.setStatus(ApoderadoDTO.getStatus());
        this.setCreateAt(ApoderadoDTO.getCreateAt());
        this.setUpdateAt(ApoderadoDTO.getUpdateAt());
        this.setDeleteAt(ApoderadoDTO.getDeleteAt());
    }
}
