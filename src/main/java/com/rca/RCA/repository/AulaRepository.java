package com.rca.RCA.repository;

import com.rca.RCA.entity.AlumnoEntity;
import com.rca.RCA.entity.ApoderadoEntity;
import com.rca.RCA.entity.AulaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface AulaRepository extends JpaRepository<AulaEntity, Integer> {

    //Función para contar las aulas existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT count(x) from GradoEntity g " +
            "JOIN g.aulaEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND s.status = :status " +
            "AND x.status = :status " +
            "AND g.status = :status " +
            "AND (s.name like concat('%', :filter, '%') or g.name like concat('%', :filter, '%') or s.code like concat('%', :filter, '%') or g.code like concat('%', :filter, '%'))")
    Long findCountAula(String status, String filter);

    //Función para listar las aulas existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT x from GradoEntity g " +
            "JOIN g.aulaEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND s.status = :status " +
            "AND x.status = :status " +
            "AND g.status = :status " +
            "AND (s.name like concat('%', :filter, '%') " +
            "or g.name like concat('%', :filter, '%') " +
            "or s.code like concat('%', :filter, '%') " +
            "or g.code like concat('%', :filter, '%')) " +
            "ORDER BY g.name, s.name")
    Optional<List<AulaEntity>> findAula(String status, String filter, Pageable pageable);

    //Función para obtener un aula con su Identificado Único
    Optional<AulaEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "SELECT x from GradoEntity g " +
            "JOIN g.aulaEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND g.id= :id_grado " +
            "AND g.status= :status ")
    Optional<List<AulaEntity>> findById_Grado(Integer id_grado, String status);

    @Query(value = "SELECT x from GradoEntity g " +
            "JOIN g.aulaEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND s.id= :id_seccion " +
            "AND s.status= :status ")
    Optional<List<AulaEntity>> findById_Seccion(Integer id_seccion, String status);

    @Query(value = "SELECT x from GradoEntity g " +
            "JOIN g.aulaEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND g.id= :id_grado " +
            "AND s.id= :id_seccion " +
            "AND x.status= :status ")
    Optional<AulaEntity> findByGradoYSeccion(Integer id_grado, Integer id_seccion, String status);


    @Query(value = "SELECT al " +
            "from AulaEntity a " +
            "JOIN a.matriculaEntities m " +
            "JOIN m.alumnoEntity al " +
            "JOIN m.anio_lectivoEntity an " +
            "JOIN al.apoderadoEntity ap " +
            "JOIN al.usuarioEntity ua " +
            "WHERE a=m.aulaEntity " +
            "AND al= m.alumnoEntity " +
            "AND ap= al.apoderadoEntity " +
            "AND ua= al.usuarioEntity " +
            "AND an= m.anio_lectivoEntity " +
            "AND a.uniqueIdentifier= :id_aula " +
            "AND an.uniqueIdentifier= :anio_lectivo " +
            "AND a.status= :status ")
    Optional<List<AlumnoEntity>> findAlumnosxAula(String id_aula, String anio_lectivo, String status);
    @Query(value = "SELECT ap " +
            "from AulaEntity a " +
            "JOIN a.matriculaEntities m " +
            "JOIN m.alumnoEntity al " +
            "JOIN m.anio_lectivoEntity an " +
            "JOIN al.apoderadoEntity ap " +
            "JOIN ap.usuarioEntity ua " +
            "WHERE a=m.aulaEntity " +
            "AND al= m.alumnoEntity " +
            "AND ap= al.apoderadoEntity " +
            "AND ua= ap.usuarioEntity " +
            "AND an= m.anio_lectivoEntity " +
            "AND a.uniqueIdentifier= :id_aula " +
            "AND an.uniqueIdentifier= :anio_lectivo " +
            "AND a.status= :status ")
    Optional<List<ApoderadoEntity>> findApoderadosxAula(String id_aula, String anio_lectivo, String status);
}