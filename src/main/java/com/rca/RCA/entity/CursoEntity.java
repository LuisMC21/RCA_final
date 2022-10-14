package com.rca.RCA.entity;

import com.rca.RCA.type.CursoDTO;
import com.rca.RCA.type.GradoDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "curso")
public class CursoEntity extends AuditoryEntity{
    //Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    //Código
    @Column(name = "cod", length = 15)
    private String code;
    //Nombre del curso
    @Column(name = "nom")
    private Character name;
    //Docentes del curso
    @OneToMany(mappedBy = "cursoEntity", cascade=CascadeType.ALL)
    private Set<DocentexCursoEntity> docentexCursoEntities = new HashSet<>();

    public CursoDTO getCursoDTO(){
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setId(this.getUniqueIdentifier());
        cursoDTO.setCode(this.code);
        cursoDTO.setName(this.name);
        cursoDTO.setStatus(this.getStatus());
        cursoDTO.setCreateAt(this.getCreateAt());
        cursoDTO.setUpdateAt(this.getUpdateAt());
        cursoDTO.setDeleteAt(this.getDeleteAt());
        return cursoDTO;
    }

    public void setCursoDTO(CursoDTO cursoDTO){
        this.setUniqueIdentifier(cursoDTO.getId());
        this.code= cursoDTO.getCode();
        this.name= cursoDTO.getName();
        this.setStatus(cursoDTO.getStatus());
        this.setCreateAt(cursoDTO.getCreateAt());
        this.setUpdateAt(cursoDTO.getUpdateAt());
        this.setDeleteAt(cursoDTO.getDeleteAt());
    }

}
