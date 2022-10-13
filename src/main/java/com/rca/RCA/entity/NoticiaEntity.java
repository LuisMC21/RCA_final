package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rca.RCA.type.NoticiaDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Noticia")
public class NoticiaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "cod", length = 40)
    private String code;
    @Column(name = "tit")
    private String tit;
    @Column(name = "sum")
    private String sum;
    @Column(name = "desc")
    private String desc;
    @Column(name = "imag")
    private String imag;
    @Column(name = "fecha")
    private String fecha;

}
