package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rca.RCA.type.NoticiaDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Noticia")
public class NoticiaEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 40)
    private String code;
    @Column(name = "title")
    private String title;
    @Column(name = "sommelier")
    private String sommelier;
    @Column(name = "descrip")
    private String descrip;
    @Column(name = "image")
    private String image;
    @Column(name = "date")
    private String date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UsuarioEntity usuarioEntity;

    public NoticiaDTO getNoticiaDTO(){
        NoticiaDTO NoticiaDTO = new NoticiaDTO();
        NoticiaDTO.setId(this.getUniqueIdentifier());
        NoticiaDTO.setCode(this.code);
        NoticiaDTO.setTitle(this.title);
        NoticiaDTO.setSommelier(this.sommelier);
        NoticiaDTO.setDescrip(this.descrip);
        NoticiaDTO.setImage(this.image);
        NoticiaDTO.setDate(this.date);
        NoticiaDTO.setUsuarioDTO(this.usuarioEntity.getUsuarioDTO());
        NoticiaDTO.setStatus(this.getStatus());
        NoticiaDTO.setCreateAt(this.getCreateAt());
        NoticiaDTO.setUpdateAt(this.getUpdateAt());
        NoticiaDTO.setDeleteAt(this.getDeleteAt());
        return NoticiaDTO;
    }

    public void setNoticiaDTO(NoticiaDTO NoticiaDTO){
        this.setUniqueIdentifier(NoticiaDTO.getId());
        this.code = NoticiaDTO.getCode();
        this.title = NoticiaDTO.getTitle();
        this.sommelier = NoticiaDTO.getSommelier();
        this.descrip = NoticiaDTO.getDescrip();
        this.image = NoticiaDTO.getImage();
        this.date = NoticiaDTO.getDate();
        this.setStatus(NoticiaDTO.getStatus());
        this.setCreateAt(NoticiaDTO.getCreateAt());
        this.setUpdateAt(NoticiaDTO.getUpdateAt());
        this.setDeleteAt(NoticiaDTO.getDeleteAt());
    }

}
