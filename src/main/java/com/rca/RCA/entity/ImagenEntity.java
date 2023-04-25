package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.ImagenDTO;
import com.rca.RCA.type.ImagenFileDTO;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "Imagen")
public class ImagenEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "name")
    @NotBlank
    private String name;
    @Column(name = "route")
    @NotBlank
    private String route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
        this.name = ImagenDTO.getName();
        this.route = ImagenDTO.getRoute();
        this.setStatus(ImagenDTO.getStatus());
        this.setCreateAt(ImagenDTO.getCreateAt());
        this.setUpdateAt(ImagenDTO.getUpdateAt());
        this.setDeleteAt(ImagenDTO.getDeleteAt());
    }
}
