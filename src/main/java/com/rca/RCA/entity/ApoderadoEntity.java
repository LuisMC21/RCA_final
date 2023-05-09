package com.rca.RCA.entity;

import com.rca.RCA.type.ApoderadoDTO;
import lombok.Data;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
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

    @Column(name = "name")
    private String name;
    @Column(name = "pa_surname")
    private String pa_surname;
    @Column(name = "ma_surname")
    private String ma_surname;
    @Column(name = "birthdate")
    private Date birthdate;
    @Column(name = "type_doc")
    private String type_doc;
    @Column(name = "numdoc")
    private String numdoc;
    @Column(name = "email", unique = true)
    private String email;
    private String tel;

    @OneToMany(mappedBy = "apoderadoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<AlumnoEntity> alumnoEntities = new HashSet<>();

    public ApoderadoDTO getApoderadoDTO(){
        ApoderadoDTO ApoderadoDTO = new ApoderadoDTO();
        ApoderadoDTO.setId(this.getUniqueIdentifier());
        ApoderadoDTO.setCode(this.code);
        ApoderadoDTO.setName(this.name);
        ApoderadoDTO.setPa_surname(this.pa_surname);
        ApoderadoDTO.setMa_surname(this.ma_surname);
        ApoderadoDTO.setBirthdate(this.birthdate);
        ApoderadoDTO.setType_doc(this.type_doc);
        ApoderadoDTO.setEmail(this.email);
        ApoderadoDTO.setTel(this.tel);
        ApoderadoDTO.setStatus(this.getStatus());
        ApoderadoDTO.setCreateAt(this.getCreateAt());
        ApoderadoDTO.setUpdateAt(this.getUpdateAt());
        ApoderadoDTO.setDeleteAt(this.getDeleteAt());
        return ApoderadoDTO;
    }

    public void setApoderadoDTO(ApoderadoDTO ApoderadoDTO){
        this.setUniqueIdentifier(ApoderadoDTO.getId());
        this.code = ApoderadoDTO.getCode();
        this.name = ApoderadoDTO.getName();
        this.pa_surname = ApoderadoDTO.getPa_surname();
        this.ma_surname = ApoderadoDTO.getMa_surname();
        this.birthdate = ApoderadoDTO.getBirthdate();
        this.type_doc = ApoderadoDTO.getType_doc();
        this.email = ApoderadoDTO.getEmail();
        this.tel = ApoderadoDTO.getTel();
        this.setStatus(ApoderadoDTO.getStatus());
        this.setCreateAt(ApoderadoDTO.getCreateAt());
        this.setUpdateAt(ApoderadoDTO.getUpdateAt());
        this.setDeleteAt(ApoderadoDTO.getDeleteAt());
    }
    public String getNameCompleto(){
        return this.pa_surname + " "+ ma_surname + " "+ this.name;
    }
}
