package com.rca.RCA.repository;

import com.rca.RCA.entity.AsistenciaEntity;
import com.rca.RCA.entity.ClaseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, Integer> {

    //Obtener asistencia por clase (código) o por alumno (nombre, apellido, código)
    @Query(value = "select a from AsistenciaEntity a JOIN a.alumnoEntity al JOIN a.claseEntity c "+
            "where al = a.alumnoEntity and c = a.claseEntity " +
            "and a.status = :status and al.status =:status and c.status = :status " +
            "and (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "or c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<AsistenciaEntity>> findEntities(String status, String filter, Pageable pageable);


    //Contar el número de registros de  asistencia según el filtro que se aplique
    @Query(value = "select count(a) from AsistenciaEntity a JOIN a.alumnoEntity al JOIN a.claseEntity c "+
            "where al = a.alumnoEntity and c = a.claseEntity " +
            "and a.status = :status and al.status =:status and c.status = :status " +
            "and (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "or c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    //Obtener una asistencia por su identificador
    Optional<AsistenciaEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "SELECT ass FROM AnioLectivoEntity a " +
                    "JOIN a.matriculaEntities l " +
                    "JOIN l.alumnoEntity al " +
                    "JOIN l.aulaEntity au " +
                    "JOIN au.docentexCursoEntities dc " +
                    "JOIN dc.docenteEntity d " +
                    "JOIN dc.cursoEntity c " +
                    "JOIN dc.claseEntities cl " +
                    "JOIN cl.periodoEntity p " +
                    "JOIN cl.asistenciaEntities ass " +
                    "WHERE a=l.anio_lectivoEntity " +
                    "AND al=l.alumnoEntity " +
                    "AND au=l.aulaEntity " +
                    "AND d= dc.docenteEntity " +
                    "AND c= dc.cursoEntity " +
                    "AND dc= cl.docentexCursoEntity " +
                    "AND p= cl.periodoEntity " +
                    "AND cl= ass.claseEntity " +
                    "AND l.status = :status " +
                    "AND d.status = :status " +
                    "AND c.status = :status " +
                    "AND al.status = :status " +
                    "AND cl.status = :status " +
                    "AND au.status = :status " +
                    "AND dc.status = :status " +
                    "AND ass.status = :status " +
                    "AND p.status = :status " +
                    "AND al.uniqueIdentifier = :id_alumno " +
                    "AND p.uniqueIdentifier = :id_periodo " +
                    "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<List<AsistenciaEntity>> findAsistencias(String id_alumno, String id_periodo, String id_aniolectivo, String status);
    @Query(value = "SELECT cl FROM AnioLectivoEntity a " +
            "JOIN a.matriculaEntities l " +
            "JOIN l.alumnoEntity al " +
            "JOIN l.aulaEntity au " +
            "JOIN au.docentexCursoEntities dc " +
            "JOIN dc.docenteEntity d " +
            "JOIN dc.cursoEntity c " +
            "JOIN dc.claseEntities cl " +
            "JOIN cl.periodoEntity p " +
            "JOIN cl.asistenciaEntities ass " +
            "WHERE a=l.anio_lectivoEntity " +
            "AND al=l.alumnoEntity " +
            "AND au=l.aulaEntity " +
            "AND d= dc.docenteEntity " +
            "AND c= dc.cursoEntity " +
            "AND dc= cl.docentexCursoEntity " +
            "AND p= cl.periodoEntity " +
            "AND cl= ass.claseEntity " +
            "AND l.status = :status " +
            "AND d.status = :status " +
            "AND c.status = :status " +
            "AND al.status = :status " +
            "AND cl.status = :status " +
            "AND au.status = :status " +
            "AND dc.status = :status " +
            "AND ass.status = :status " +
            "AND p.status = :status " +
            "AND al.uniqueIdentifier = :id_alumno " +
            "AND p.uniqueIdentifier = :id_periodo " +
            "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<List<ClaseEntity>> findClasesDeAsistencias(String id_alumno, String id_periodo, String id_aniolectivo, String status);
}
