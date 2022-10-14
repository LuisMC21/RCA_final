package com.rca.RCA.entity;

import com.rca.RCA.type.UsuarioDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Usuario")
public class UsuarioEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser", unique = true, nullable = false)
    private Integer id;
    @Column(name = "code", length = 40)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "pa_surname")
    private String pa_surname;
    @Column(name = "ma_surname")
    private String ma_surname;
    @Column(name = "type_doc")
    private String type_doc;
    @Column(name = "num_doc")
    private String num_doc;
    @Column(name = "tel")
    private String tel;
    @Column(name = "gra_inst")
    private String gra_inst;
    @Column(name = "email_inst")
    private String email_inst;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "idrol")
    private RolEntity rolEntity;


    public UsuarioDTO getUsuarioDTO(){
        UsuarioDTO UsuarioDTO = new UsuarioDTO();
        UsuarioDTO.setId(this.getUniqueIdentifier());
        UsuarioDTO.setCode(this.code);
        UsuarioDTO.setName(this.name);
        UsuarioDTO.setPa_surname(this.pa_surname);
        UsuarioDTO.setMa_surname(this.ma_surname);
        UsuarioDTO.setType_doc(this.type_doc);
        UsuarioDTO.setNum_doc(this.num_doc);
        UsuarioDTO.setTel(this.tel);
        UsuarioDTO.setGra_inst(this.gra_inst);
        UsuarioDTO.setEmail_ins(this.email_inst);
        UsuarioDTO.setRolDTO(this.rolEntity.getRolDTO());
        UsuarioDTO.setStatus(this.getStatus());
        UsuarioDTO.setCreateAt(this.getCreateAt());
        UsuarioDTO.setUpdateAt(this.getUpdateAt());
        UsuarioDTO.setDeleteAt(this.getDeleteAt());
        return UsuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO UsuarioDTO){
        this.setUniqueIdentifier(UsuarioDTO.getId());
        this.code = UsuarioDTO.getCode();
        this.name = UsuarioDTO.getName();
        this.pa_surname = UsuarioDTO.getPa_surname();
        this.ma_surname = UsuarioDTO.getMa_surname();
        this.type_doc = UsuarioDTO.getType_doc();
        this.num_doc = UsuarioDTO.getNum_doc();
        this.tel = UsuarioDTO.getTel();
        this.gra_inst = UsuarioDTO.getGra_inst();
        this.email_inst = UsuarioDTO.getEmail_ins();
        this.setStatus(UsuarioDTO.getStatus());
        this.setCreateAt(UsuarioDTO.getCreateAt());
        this.setUpdateAt(UsuarioDTO.getUpdateAt());
        this.setDeleteAt(UsuarioDTO.getDeleteAt());
    }
}
