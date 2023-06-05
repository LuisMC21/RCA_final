package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rca.RCA.type.ApoderadoDTO;
import lombok.Data;

import jakarta.persistence.*;

import java.time.LocalDate;
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
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "birthdate")
    private LocalDate birthdate;
    @Column(name = "type_doc")
    private String type_doc;
    @Column(name = "numdoc")
    private String numdoc;
    @Column(name = "email")
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
        ApoderadoDTO.setNumdoc(this.numdoc);
        ApoderadoDTO.setEmail(this.email);
        ApoderadoDTO.setTel(this.tel);
        ApoderadoDTO.setNumdoc(this.numdoc);
        ApoderadoDTO.setStatus(this.getStatus());
        ApoderadoDTO.setCreateAt(this.getCreateAt());
        ApoderadoDTO.setUpdateAt(this.getUpdateAt());
        ApoderadoDTO.setDeleteAt(this.getDeleteAt());
        return ApoderadoDTO;
    }

    public void setApoderadoDTO(ApoderadoDTO apoderadoDTO) {
        this.setUniqueIdentifier(apoderadoDTO.getId());
        this.code = apoderadoDTO.getCode();
        this.name = apoderadoDTO.getName();
        this.pa_surname = apoderadoDTO.getPa_surname();
        this.ma_surname = apoderadoDTO.getMa_surname();
        this.birthdate = apoderadoDTO.getBirthdate();
        this.type_doc = apoderadoDTO.getType_doc();
        this.email = apoderadoDTO.getEmail();
        this.numdoc = apoderadoDTO.getNumdoc();
        this.tel = apoderadoDTO.getTel();
        this.setStatus(apoderadoDTO.getStatus());
        this.setCreateAt(apoderadoDTO.getCreateAt());
        this.setUpdateAt(apoderadoDTO.getUpdateAt());
        this.setDeleteAt(apoderadoDTO.getDeleteAt());
    }
    public String getNameCompleto(){
        return this.pa_surname + " "+ ma_surname + " "+ this.name;
    }
}
