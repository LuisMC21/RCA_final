package com.rca.RCA.entity;

import com.rca.RCA.type.ApoderadoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Apoderado")
public class ApoderadoEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "email", unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UsuarioEntity usuarioEntity;

    @OneToMany(mappedBy = "apoderadoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<AlumnoEntity> alumnoEntities = new HashSet<>();

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
