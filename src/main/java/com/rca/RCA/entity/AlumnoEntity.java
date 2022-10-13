package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rca.RCA.type.AlumnoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Alumno")
public class AlumnoEntity extends AuditoryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "cod", length = 40)
    private String code;
    @Column(name = "appaterno")
    private String appaterno;
    @Column(name = "apmaterno")
    private String apmaterno;
    @Column(name = "tip_doc")
    private String tip_doc;
    @Column(name = "num_doc")
    private String num_doc;
    @Column(name = "tel")
    private String tel;
    @Column(name = "gra_inst")
    private String gra_inst;
    @Column(name = "cor_inst")
    private String cor_inst;
}
