package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rca.RCA.type.ImagenDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Imagen")
public class ImagenEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idimagen", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "route")
    private String route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "idusuario")
    private UsuarioEntity usuarioEntity;

    public ImagenDTO getImagenDTO(){
        ImagenDTO ImagenDTO = new ImagenDTO();
        ImagenDTO.setId(this.getUniqueIdentifier());
        ImagenDTO.setCode(this.code);
        ImagenDTO.setName(this.name);
        ImagenDTO.setRoute(this.route);
        ImagenDTO.setUsuarioDTO(this.usuarioEntity.getUsuarioDTO());
        ImagenDTO.setStatus(this.getStatus());
        ImagenDTO.setCreateAt(this.getCreateAt());
        ImagenDTO.setUpdateAt(this.getUpdateAt());
        ImagenDTO.setDeleteAt(this.getDeleteAt());
        return ImagenDTO;
    }

    public void setImagenDTO(ImagenDTO ImagenDTO){
        this.setUniqueIdentifier(ImagenDTO.getId());
        this.code = ImagenDTO.getCode();
        this.name = ImagenDTO.getName();
        this.route = ImagenDTO.getRoute();
        this.setStatus(ImagenDTO.getStatus());
        this.setCreateAt(ImagenDTO.getCreateAt());
        this.setUpdateAt(ImagenDTO.getUpdateAt());
        this.setDeleteAt(ImagenDTO.getDeleteAt());
    }
}
