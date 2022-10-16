package com.rca.RCA.entity;

import com.rca.RCA.type.UsuarioDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Usuario")
public class UsuarioEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 15)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "pa_surname")
    private String pa_surname;
    @Column(name = "ma_surname")
    private String ma_surname;
    @Column(name = "type_doc")
    private String type_doc;
    @Column(name = "numdoc")
    private String numdoc;
    @Column(name = "tel")
    private String tel;
    @Column(name = "gra_inst")
    private String gra_inst;
    @Column(name = "email_inst")
    private String email_inst;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "rol_id")
    private RolEntity rolEntity;

    @OneToMany(mappedBy = "usuarioEntity", cascade=CascadeType.ALL)
    private Set<ImagenEntity> imagenEntities = new HashSet<>();

    @OneToMany(mappedBy = "usuarioEntity", cascade=CascadeType.ALL)
    private Set<NoticiaEntity> noticiaEntities = new HashSet<>();

    @OneToOne(mappedBy = "usuarioEntity")
    private ApoderadoEntity apoderadoEntity;

    @OneToOne(mappedBy = "usuarioEntity")
    private AlumnoEntity alumnoEntity;

    @OneToOne(mappedBy = "usuarioEntity")
    private DocenteEntity docenteEntity;


    public UsuarioDTO getUsuarioDTO(){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(this.getUniqueIdentifier());
        usuarioDTO.setCode(this.code);
        usuarioDTO.setName(this.name);
        usuarioDTO.setPa_surname(this.pa_surname);
        usuarioDTO.setMa_surname(this.ma_surname);
        usuarioDTO.setType_doc(this.type_doc);
        usuarioDTO.setNumdoc(this.numdoc);
        usuarioDTO.setTel(this.tel);
        usuarioDTO.setGra_inst(this.gra_inst);
        usuarioDTO.setEmail_ins(this.email_inst);
        usuarioDTO.setRolDTO(this.rolEntity.getRolDTO());
        usuarioDTO.setApoderadoDTO(this.apoderadoEntity.getApoderadoDTO());
        usuarioDTO.setAlumnoDTO(this.alumnoEntity.getAlumnoDTO());
        usuarioDTO.setDocenteDTO(this.docenteEntity.getDocenteDTO());
        usuarioDTO.setStatus(this.getStatus());
        usuarioDTO.setCreateAt(this.getCreateAt());
        usuarioDTO.setUpdateAt(this.getUpdateAt());
        usuarioDTO.setDeleteAt(this.getDeleteAt());
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO UsuarioDTO){
        this.setUniqueIdentifier(UsuarioDTO.getId());
        this.code = UsuarioDTO.getCode();
        this.name = UsuarioDTO.getName();
        this.pa_surname = UsuarioDTO.getPa_surname();
        this.ma_surname = UsuarioDTO.getMa_surname();
        this.type_doc = UsuarioDTO.getType_doc();
        this.numdoc = UsuarioDTO.getNumdoc();
        this.tel = UsuarioDTO.getTel();
        this.gra_inst = UsuarioDTO.getGra_inst();
        this.email_inst = UsuarioDTO.getEmail_ins();
        this.setStatus(UsuarioDTO.getStatus());
        this.setCreateAt(UsuarioDTO.getCreateAt());
        this.setUpdateAt(UsuarioDTO.getUpdateAt());
        this.setDeleteAt(UsuarioDTO.getDeleteAt());
    }
}
