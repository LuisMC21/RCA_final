package com.rca.RCA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rca.RCA.type.RolDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Rol")
public class RolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "cod", length = 40)
    private String code;
    @Column(name = "nom")
    private String nombre;
}
