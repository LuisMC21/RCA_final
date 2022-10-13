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
public class ImagenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "cod", length = 40)
    private String code;
    @Column(name = "nom")
    private String nombre;
    @Column(name = "ruta")
    private String ruta;
}
