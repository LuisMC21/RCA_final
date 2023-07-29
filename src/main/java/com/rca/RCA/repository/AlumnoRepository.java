package com.rca.RCA.repository;

import com.rca.RCA.entity.AlumnoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
            "u.uniqueIdentifier like concat('%', :filter, '%') or " +
            "u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Optional<List<AlumnoEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value="SELECT a FROM MatriculaEntity m " +
            "JOIN m.alumnoEntity a " +
            "JOIN m.aulaEntity al " +
            "JOIN m.anio_lectivoEntity an " +
            "WHERE a.status = :status " +
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
    @Query(value = "Select a.* from alumno a JOIN matricula m ON a.id = m.alumno_id " +
            "join aula al ON al.id = m.aula_id " +
            "join docentexcurso dxc ON dxc.aula_id = al.id " +
            "join curso c ON c.id = dxc.curso_id " +
            "join anio_lectivo ale ON ale.id = m.anio_lectivo_id " +
            "where a.tx_status = :status " +
            "and al.tx_unique_identifier like concat('%', :aula, '%') and " +
            "c.tx_unique_identifier like concat('%', :curso, '%') " +
            "and ale.tx_unique_identifier like concat('%', :anio, '%')", nativeQuery = true)
    Optional<List<AlumnoEntity>> findEntities(String status, String anio, String aula, String curso);

    @Query(value = "SELECT a FROM AlumnoEntity a " +
            "JOIN a.matriculaEntities m " +
            "JOIN a.usuarioEntity u " +
            "JOIN m.aulaEntity au " +
            "join au.docentexCursoEntities dxc " +
            "JOIN m.anio_lectivoEntity ale " +
            "where a.status = :status " +
            "AND m.status = :status " +
            "AND u.status = :status " +
            "AND au.status = :status " +
            "AND dxc.status = :status " +
            "AND ale.status = :status " +
            "and (u.numdoc like concat('%', :filter, '%') " +
            "or u.pa_surname like concat('%', :filter, '%') " +
            "or u.name like concat('%', :filter, '%') " +
            "or u.ma_surname like concat('%', :filter, '%'))  " +
            "and au.uniqueIdentifier like concat('%', :aula, '%') " +
            "and ale.uniqueIdentifier like concat('%', :anio, '%')")
    Optional<List<AlumnoEntity>> findEntities(String filter, String status, String anio, String aula, Pageable pageable);

    @Query(value = "SELECT count(a) FROM AlumnoEntity a " +
            "JOIN a.matriculaEntities m " +
            "JOIN a.usuarioEntity u " +
            "JOIN m.aulaEntity au " +
            "join au.docentexCursoEntities dxc " +
            "JOIN m.anio_lectivoEntity ale " +
            "where a.status = :status " +
            "AND m.status = :status " +
            "AND u.status = :status " +
            "AND au.status = :status " +
            "AND dxc.status = :status " +
            "AND ale.status = :status " +
            "and (u.numdoc like concat('%', :filter, '%') " +
            "or u.pa_surname like concat('%', :filter, '%') " +
            "or u.name like concat('%', :filter, '%') " +
            "or u.ma_surname like concat('%', :filter, '%'))  " +
            "and au.uniqueIdentifier like concat('%', :aula, '%') " +
            "and ale.uniqueIdentifier like concat('%', :anio, '%')")
    Long findCountEntities(String filter, String status, String anio, String aula);

    //Función para obtener un alumno por su identificador
    @Query(value = "SELECT a FROM AlumnoEntity a " +
            "WHERE a.uniqueIdentifier = :uniqueIdentifier " +
            "AND a.status = 'CREATED' ")
    Optional<AlumnoEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<AlumnoEntity> findByCode(String code);

    //Función para eliminar usuario asociado al alumno
    @Transactional
    @Modifying
    @Query(value = "update user u JOIN alumno a  SET u.tx_status = 'DELETED', u.tx_delete_at = :fecha, " +
            "a.tx_status = 'DELETED', a.tx_delete_at = :fecha " +
            "where a.user_id = u.id " +
            "and a.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void deleteUsuario(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha")LocalDateTime fecha);

    //Función para eliminar asistencias asociadas al alumno
    @Transactional
    @Modifying
    @Query(value = "update asistencia a JOIN alumno al SET a.tx_status = 'DELETED', a.tx_delete_at = :fecha " +
            "where a.alumno_id = al.id " +
            "and al.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void deleteAsistencia(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha") LocalDateTime fecha);

    //Función para eliminar evaluaciones asociadas al alumno
    @Transactional
    @Modifying
    @Query(value = "update evaluacion e JOIN alumno al SET e.tx_status = 'DELETED', e.tx_delete_at = :fecha " +
            "where e.alumno_id = al.id " +
            "and al.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void deleteEvaluciones(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha") LocalDateTime fecha);

    @Query(value="Select * from alumno where apoderado_id = :idApo", nativeQuery = true)
    Optional<List<AlumnoEntity>> findByApoderado(int idApo);

    @Query(value="Select * from alumno where apoderado_id = :idApo", nativeQuery = true)
    Iterable<AlumnoEntity> findByApoderadoI(int idApo);


    @Query(value="SELECT a.* FROM alumno a join matricula m on a.id = m.alumno_id join " +
            "aula al on al.id = m.aula_id join anio_lectivo ale on " +
            "ale.id = m.anio_lectivo_id where al.tx_unique_identifier = :aula " +
            "AND a.tx_status = :status " +
            "AND al.tx_status = :status " +
            "AND m.tx_status = :status " +
            "AND ale.tx_status = :status " +
            "and ale.tx_unique_identifier = :anio", nativeQuery=true)
    Optional<List<AlumnoEntity>> findByAulaPeriodo(String aula, String anio, String status);
    @Query(value="SELECT a.* FROM alumno a join matricula m on a.id = m.alumno_id join " +
            "aula al on al.id = m.aula_id join anio_lectivo ale on " +
            "ale.id = m.anio_lectivo_id where al.tx_unique_identifier = :aula " +
            "AND a.tx_status = :status " +
            "AND al.tx_status = :status " +
            "AND m.tx_status = :status " +
            "AND ale.tx_status = :status " +
            "and ale.tx_unique_identifier = :anio", nativeQuery=true)
    Iterable<AlumnoEntity> findByAulaPeriodoI(String aula, String anio, String status);


}