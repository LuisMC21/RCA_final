package com.rca.RCA.repository;

import com.rca.RCA.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<MatriculaEntity, Integer> {

    //Función para contar las aulas existentes y activas de una matricula, con filtro de código y nombre
    @Query(value = "SELECT count(l) from AlumnoEntity al " +
            "JOIN al.matriculaEntities l " +
            "JOIN l.aulaEntity au " +
            "JOIN l.anio_lectivoEntity an " +
            "JOIN al.usuarioEntity u " +
            "WHERE al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND an=l.anio_lectivoEntity " +
            "AND l.status = :status " +
            "AND (al.code like concat('%', :filter, '%') " +
            "or l.code like concat('%', :filter, '%') " +
            "or au.code like concat('%', :filter, '%') " +
            "or an.code like concat('%', :filter, '%') " +
            "or u.code like concat('%', :filter, '%') " +
            "or u.pa_surname like concat('%', :filter, '%') " +
            "or u.name like concat('%', :filter, '%')" +
            "or an.name like concat('%', :filter, '%'))")
    Long findCountMatricula(String status, String filter);

    //Función para listar las aulas existentes y activas de una matricula, con filtro de código y nombre
    @Query(value = "SELECT l from AlumnoEntity al " +
            "JOIN al.matriculaEntities l " +
            "JOIN l.aulaEntity au " +
            "JOIN l.anio_lectivoEntity an " +
            "JOIN al.usuarioEntity u " +
            "WHERE al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND an=l.anio_lectivoEntity " +
            "AND l.status = :status " +
            "AND (al.code like concat('%', :filter, '%') " +
            "or l.code like concat('%', :filter, '%') " +
            "or au.code like concat('%', :filter, '%') " +
            "or an.code like concat('%', :filter, '%') " +
            "or u.code like concat('%', :filter, '%') " +
            "or u.pa_surname like concat('%', :filter, '%') " +
            "or u.name like concat('%', :filter, '%')" +
            "or an.name like concat('%', :filter, '%'))")
    Optional<List<MatriculaEntity>> findMatricula(String status, String filter, Pageable pageable);

    //Función para obtener una matricula con su Identificado Único
    Optional<MatriculaEntity> findByUniqueIdentifier(String uniqueIdentifier);
    @Query(value = "SELECT l from AlumnoEntity al " +
            "JOIN al.matriculaEntities l " +
            "JOIN l.aulaEntity au " +
            "JOIN l.anio_lectivoEntity an " +
            "JOIN al.usuarioEntity u " +
            "WHERE al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND an=l.anio_lectivoEntity " +
            "AND l.status = :status " +
            "AND au.status = :status " +
            "AND an.status = :status " +
            "AND au.uniqueIdentifier= :id_aula " +
            "AND al.uniqueIdentifier= :id_alumno " +
            "AND an.uniqueIdentifier= :id_anioLectivo " +
            "ORDER BY au")
    Optional<MatriculaEntity> findByAuAlAn(String id_aula, String id_alumno, String id_anioLectivo, String status);

    @Query(value = "SELECT l from AulaEntity a " +
            "JOIN a.matriculaEntities l " +
            "WHERE a=l.aulaEntity " +
            "AND l.status = :status " +
            "AND a.status = :status " +
            "AND a.uniqueIdentifier= :id_aula ")
    Optional<List<MatriculaEntity>> findByAula(String id_aula, String status);

    @Query(value = "SELECT l FROM AnioLectivoEntity a " +
            "JOIN a.matriculaEntities l " +
            "WHERE a=l.anio_lectivoEntity " +
            "AND l.status = :status " +
            "AND a.status = :status " +
            "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<List<MatriculaEntity>> findByAnioLectivo(String id_aniolectivo, String status);

    @Query(value = "SELECT c FROM AnioLectivoEntity a " +
            "JOIN a.matriculaEntities l " +
            "JOIN l.alumnoEntity al " +
            "JOIN l.aulaEntity au " +
            "JOIN au.docentexCursoEntities dc " +
            "JOIN dc.docenteEntity d " +
            "JOIN dc.cursoEntity c " +
            "WHERE a=l.anio_lectivoEntity " +
            "AND al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND au= dc.aulaEntity " +
            "AND d= dc.docenteEntity " +
            "AND c= dc.cursoEntity " +
            "AND l.status = :status " +
            "AND a.status = :status " +
            "AND al.uniqueIdentifier = :id_alumno " +
            "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<List<CursoEntity>> findCursosMatriculados(String id_alumno, String id_aniolectivo, String status);
    @Query(value = "SELECT d FROM AnioLectivoEntity a " +
            "JOIN a.matriculaEntities l " +
            "JOIN l.alumnoEntity al " +
            "JOIN l.aulaEntity au " +
            "JOIN au.docentexCursoEntities dc " +
            "JOIN dc.docenteEntity d " +
            "JOIN dc.cursoEntity c " +
            "WHERE a=l.anio_lectivoEntity " +
            "AND al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND au= dc.aulaEntity " +
            "AND d= dc.docenteEntity " +
            "AND c= dc.cursoEntity " +
            "AND l.status = :status " +
            "AND a.status = :status " +
            "AND al.uniqueIdentifier = :id_alumno " +
            "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<List<DocenteEntity>> findDocentesdeCurosMatriculados(String id_alumno, String id_aniolectivo, String status);
    @Query(value = "SELECT g FROM AnioLectivoEntity a " +
            "JOIN a.matriculaEntities l " +
            "JOIN l.alumnoEntity al " +
            "JOIN l.aulaEntity au " +
            "JOIN au.gradoEntity g " +
            "JOIN au.seccionEntity s " +
            "JOIN au.docentexCursoEntities dc " +
            "JOIN dc.docenteEntity d " +
            "JOIN dc.cursoEntity c " +
            "WHERE a=l.anio_lectivoEntity " +
            "AND al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND au= dc.aulaEntity " +
            "AND d= dc.docenteEntity " +
            "AND c= dc.cursoEntity " +
            "AND l.status = :status " +
            "AND a.status = :status " +
            "AND al.uniqueIdentifier = :id_alumno " +
            "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<GradoEntity> findGradoMatriculado(String id_alumno, String id_aniolectivo, String status);
    @Query(value = "SELECT s FROM AnioLectivoEntity a " +
            "JOIN a.matriculaEntities l " +
            "JOIN l.alumnoEntity al " +
            "JOIN l.aulaEntity au " +
            "JOIN au.gradoEntity g " +
            "JOIN au.seccionEntity s " +
            "JOIN au.docentexCursoEntities dc " +
            "JOIN dc.docenteEntity d " +
            "JOIN dc.cursoEntity c " +
            "WHERE a=l.anio_lectivoEntity " +
            "AND al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND au= dc.aulaEntity " +
            "AND d= dc.docenteEntity " +
            "AND c= dc.cursoEntity " +
            "AND l.status = :status " +
            "AND a.status = :status " +
            "AND al.uniqueIdentifier = :id_alumno " +
            "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<SeccionEntity> findSeccionMatriculado(String id_alumno, String id_aniolectivo, String status);
}
