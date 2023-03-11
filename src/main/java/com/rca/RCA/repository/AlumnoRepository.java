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

    //Función para eliminar usuario asociado al alumno
    @Transactional
    @Modifying
    @Query(value = "update usuario u JOIN alumno a  SET u.tx_status = 'DELETED', u.tx_delete_at = :fecha " +
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


    @Query(value="select * from alumno a join matricula m on a.id = m.alumno_id join aula al on al.id = m.aula_id join anio_lectivo ale on " +
            "ale.id = m.anio_lectivo_id where al.tx_unique_identifier = :aula " +
            "and ale.tx_unique_identifier = :periodo", nativeQuery=true)
    Optional<List<AlumnoEntity>> findByAulaPeriodo(String aula, String periodo);
    @Query(value="select * from alumno a join matricula m on a.id = m.alumno_id join aula al on al.id = m.aula_id join anio_lectivo ale on " +
            "ale.id = m.anio_lectivo_id where al.tx_unique_identifier = :aula " +
            "and ale.tx_unique_identifier = :periodo", nativeQuery=true)
    Iterable<AlumnoEntity> findByAulaPeriodoI(String aula, String periodo);


}