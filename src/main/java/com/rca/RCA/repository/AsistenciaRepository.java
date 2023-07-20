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
    @Query(value = "select a from AsistenciaEntity a JOIN a.alumnoEntity al JOIN a.claseEntity c " +
            "where al = a.alumnoEntity and c = a.claseEntity " +
            "and a.status = :status and al.status =:status and c.status = :status " +
            "and (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "or c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<AsistenciaEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "SELECT a.* " +
            "FROM asistencia a " +
            "JOIN clase c ON a.clase_id = c.id " +
            "JOIN docentexcurso dc ON c.docentexcurso_id = dc.id " +
            "JOIN curso cu ON dc.curso_id = cu.id " +
            "JOIN aula au ON dc.aula_id = au.id " +
            "JOIN periodo p ON c.periodo_id = p.id " +
            "WHERE p.tx_unique_identifier like concat('%',:periodo,'%') " +
            "AND au.tx_unique_identifier like concat('%',:aula,'%') " +
            "AND cu.tx_unique_identifier like concat('%',:curso,'%') AND a.tx_status=:status", nativeQuery = true)
    Optional<List<AsistenciaEntity>> findEntities(String status, String periodo, String aula, String curso, Pageable pageable);

    @Query(value = "SELECT a FROM AsistenciaEntity a " +
            "JOIN a.alumnoEntity al " +
            "JOIN a.claseEntity c " +
            "JOIN c.docentexCursoEntity dxc " +
            "JOIN dxc.cursoEntity course " +
            "JOIN c.periodoEntity p " +
            "WHERE a.status = :status and al.status =:status and c.status = :status " +
            "AND al.uniqueIdentifier = :alumno " +
            "AND p.uniqueIdentifier = :periodo " +
            "AND course.uniqueIdentifier = :curso " +
            "AND (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "OR c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<AsistenciaEntity>> findEntitiesWithAlumno(String status, String filter, String periodo, String alumno, String curso, Pageable pageable);
    @Query(value = "SELECT count(a) FROM AsistenciaEntity a " +
            "JOIN a.alumnoEntity al " +
            "JOIN a.claseEntity c " +
            "JOIN c.docentexCursoEntity dxc " +
            "JOIN dxc.cursoEntity course " +
            "JOIN c.periodoEntity p " +
            "WHERE a.status = :status and al.status =:status and c.status = :status " +
            "AND al.uniqueIdentifier = :alumno " +
            "AND p.uniqueIdentifier = :periodo " +
            "AND course.uniqueIdentifier = :curso " +
            "AND (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "OR c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountEntitiesWithAlumno(String status, String filter, String periodo, String alumno, String curso);

    //Contar el número de registros de  asistencia según el filtro que se aplique
    @Query(value = "select count(a) from AsistenciaEntity a JOIN a.alumnoEntity al JOIN a.claseEntity c "+
            "where al = a.alumnoEntity and c = a.claseEntity " +
            "and a.status = :status and al.status =:status and c.status = :status " +
            "and (al.code like concat('%', :filter, '%') or al.uniqueIdentifier like concat('%', :filter, '%') " +
            "or c.code like concat('%', :filter, '%') or c.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    @Query(value = "SELECT count(*)" +
            "FROM asistencia a " +
            "JOIN clase c ON a.clase_id = c.id " +
            "JOIN docentexcurso dc ON c.docentexcurso_id = dc.id " +
            "JOIN curso cu ON dc.curso_id = cu.id " +
            "JOIN aula au ON dc.aula_id = au.id " +
            "JOIN periodo p ON c.periodo_id = p.id " +
            "WHERE p.tx_unique_identifier like concat('%',:periodo,'%') " +
            "AND au.tx_unique_identifier like concat('%',:aula,'%') " +
            "AND cu.tx_unique_identifier like concat('%',:curso,'%')AND a.tx_status=:status", nativeQuery = true)
    Long findCountEntities(String status, String periodo, String aula, String curso);

    //Obtener una asistencia por su identificador
    Optional<AsistenciaEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "SELECT ass FROM AsistenciaEntity ass " +
                    "JOIN ass.alumnoEntity al " +
                    "JOIN ass.claseEntity cl " +
                    "JOIN cl.periodoEntity p " +
                    "JOIN p.anio_lectivoEntity a " +
                    "WHERE ass.status = :status " +
                    "AND al.status = :status " +
                    "AND cl.status = :status " +
                    "AND p.status = :status " +
                    "AND al.uniqueIdentifier = :id_alumno " +
                    "AND p.uniqueIdentifier = :id_periodo " +
                    "AND a.uniqueIdentifier= :id_aniolectivo ")
    Optional<List<AsistenciaEntity>> findAsistencias(String id_alumno, String id_periodo, String id_aniolectivo, String status);
    @Query(value = "SELECT count(distinct(ass)) FROM AsistenciaEntity ass " +
            "JOIN ass.claseEntity cl " +
            "JOIN ass.alumnoEntity al " +
            "JOIN cl.docentexCursoEntity dxc " +
            "JOIN dxc.cursoEntity c " +
            "JOIN dxc.aulaEntity au " +
            "JOIN al.matriculaEntities m " +
            "JOIN m.anio_lectivoEntity a " +
            "WHERE ass.status = :status " +
            "AND ass.state = :state " +
            "AND al.uniqueIdentifier = :id_alumno " +
            "AND c.uniqueIdentifier = :id_curso " +
            "AND au.uniqueIdentifier = :id_aula " +
            "AND a.uniqueIdentifier= :id_aniolectivo ")
    int countAsistenciasAulaAño(String id_alumno, String state, String id_curso, String id_aula, String id_aniolectivo, String status);


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

    @Query(value = "SELECT ass FROM AsistenciaEntity ass " +
            "JOIN ass.claseEntity c " +
            "WHERE ass.status = :status " +
            "AND c.uniqueIdentifier = :id_clase")
    Optional<List<AsistenciaEntity>> findByClase(String id_clase, String status);

    @Query(value = "SELECT ass FROM AsistenciaEntity ass " +
            "JOIN ass.claseEntity c " +
            "JOIN ass.alumnoEntity al " +
            "JOIN al.usuarioEntity u " +
            "WHERE ass.status = :status " +
            "AND (u.name like concat('%', :filter, '%') " +
            "OR u.pa_surname like concat('%', :filter, '%') " +
            "OR u.numdoc like concat('%', :filter, '%')) " +
            "AND c.uniqueIdentifier = :id_clase")
    Optional<List<AsistenciaEntity>> findAsistenciaByClase(String filter, String id_clase, String status, Pageable pageable);
    @Query(value = "SELECT count(ass) FROM AsistenciaEntity ass " +
            "JOIN ass.claseEntity c " +
            "JOIN ass.alumnoEntity al " +
            "JOIN al.usuarioEntity u " +
            "WHERE ass.status = :status " +
            "AND (u.name like concat('%', :filter, '%') " +
            "OR u.pa_surname like concat('%', :filter, '%') " +
            "OR u.numdoc like concat('%', :filter, '%')) " +
            "AND c.uniqueIdentifier = :id_clase")
    Long findCountAsistenciaByClase(String  filter, String id_clase, String status);

}
