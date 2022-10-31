package com.rca.RCA.repository;

import com.rca.RCA.entity.AlumnoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<AlumnoEntity, Integer> {

    //Función para obtener los alumnos con filtro por nombre, apellidos, documento
    @Query(value = "SELECT a FROM UsuarioEntity u " +
            "JOIN u.alumnoEntity a " +
            "WHERE u = a.usuarioEntity " +
            "AND a.status = :status " +
            "AND u.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
            "u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Optional<List<AlumnoEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value="SELECT a FROM MatriculaEntity m JOIN m.alumnoEntity a JOIN m.aulaEntity al JOIN m.anio_lectivoEntity an " +
            "WHERE a = m.alumnoEntity and al = m.aulaEntity and an = m.anio_lectivoEntity " +
            "and a.status = :status " +
            "and m.status = :status " +
            "and al.status = :status " +
            "and an.status = :status " +
            "and (al.code like concat('%', :aula, '%') and an.code = :anio)")
    Optional<List<AlumnoEntity>> findEntitiesAula(String status, String aula, String anio);

    @Query(value = "SELECT count(a) FROM MatriculaEntity m JOIN m.alumnoEntity a JOIN m.aulaEntity al JOIN m.anio_lectivoEntity an " +
            "WHERE a = m.alumnoEntity and al = m.aulaEntity and an = m.anio_lectivoEntity " +
            "and a.status = :status " +
            "and m.status = :status " +
            "and al.status = :status " +
            "and an.status = :status " +
            "and (al.code like concat('%', :aula, '%') and an.code = :anio)")
    Long findCountEntitiesAula(String status, String aula, String anio);

    //Función para contar los alumnos
    @Query(value = "SELECT count(a) FROM UsuarioEntity u " +
            "JOIN u.alumnoEntity a " +
            "WHERE u = a.usuarioEntity " +
            "AND a.status = :status " +
            "AND u.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
            "u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    //Función para obtener un alumno por su identificador
    Optional<AlumnoEntity> findByUniqueIdentifier(String uniqueIdentifier);

}