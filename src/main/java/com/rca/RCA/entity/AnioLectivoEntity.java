package com.rca.RCA.entity;

import com.rca.RCA.type.AnioLectivoDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "anio_lectivo")
public class AnioLectivoEntity extends AuditoryEntity{
    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "code", length = 15)
    private String code;
    //Nombre del año
    @Column(name = "name", length = 4)
    private String name;
    //Matrículas del año
    @OneToMany(mappedBy = "anio_lectivoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<MatriculaEntity> matriculaEntities = new HashSet<>();
    //Periodos del año
    @OneToMany(mappedBy = "anio_lectivoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<PeriodoEntity> periodoEntities = new HashSet<>();
    //Asignaturas del año
    @OneToMany(mappedBy = "anio_lectivoEntity", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<DocentexCursoEntity> docentexCursoEntities = new HashSet<>();

    public AnioLectivoDTO getAnioLectivoDTO(){
        AnioLectivoDTO anio_lectivoDTO = new AnioLectivoDTO();
        anio_lectivoDTO.setId(this.getUniqueIdentifier());
        anio_lectivoDTO.setCode(this.code);
        anio_lectivoDTO.setName(this.name);
        anio_lectivoDTO.setStatus(this.getStatus());
        anio_lectivoDTO.setCreateAt(this.getCreateAt());
        anio_lectivoDTO.setUpdateAt(this.getUpdateAt());
        anio_lectivoDTO.setDeleteAt(this.getDeleteAt());
        return anio_lectivoDTO;
    }
    public void setAnioLectivoDTO(AnioLectivoDTO anio_lectivoDTO){
        this.setUniqueIdentifier(anio_lectivoDTO.getId());
        this.code=anio_lectivoDTO.getCode();
        this.name=anio_lectivoDTO.getName();
        this.setStatus(anio_lectivoDTO.getStatus());
        this.setCreateAt(anio_lectivoDTO.getCreateAt());
        this.setUpdateAt(anio_lectivoDTO.getUpdateAt());
        this.setDeleteAt(anio_lectivoDTO.getDeleteAt());
    }
}
