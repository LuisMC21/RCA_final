package com.rca.RCA.repository;

import com.rca.RCA.entity.EvaluacionEntity;
import com.rca.RCA.type.CursoEvaluacionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EvaluacionRepository extends JpaRepository<EvaluacionEntity, Integer> {

    //Obtener evaluaciones por docentexCurso o por alumno
    @Query(value = "Select e.* from evaluacion e " +
            "join periodo p on e.periodo_id = p.id " +
            "join docentexcurso dxc on dxc.id = e.docentexcurso_id " +
            "join curso c on c.id = dxc.curso_id " +
            "join aula a on a.id = dxc.aula_id " +
            "join alumno al on al.id = e.alumno_id " +
            "join user u on u.id = al.user_id " +
            "where p.tx_status = :status " +
            "AND e.tx_status = :status " +
            "AND c.tx_status = :status " +
            "AND al.tx_status = :status " +
            "AND a.tx_status = :status " +
            "AND u.tx_status = :status " +
            "AND dxc.tx_status = :status " +
            "and (u.numdoc like concat('%', :filter, '%') " +
            "or u.pa_surname like concat('%', :filter, '%') " +
            "or u.name like concat('%', :filter, '%') " +
            "or u.ma_surname like concat('%', :filter, '%'))  " +
            "and  c.tx_unique_identifier like concat('%', :curso, '%') " +
            "and p.tx_unique_identifier  like concat('%', :periodo, '%') " +
            "and a.tx_unique_identifier  like concat('%', :aula, '%') " +
            "and e.tx_status = :status", nativeQuery = true)
    Optional<List<EvaluacionEntity>> findEntities(String filter, String status, String periodo, String aula, String curso,Pageable pageable);


    @Query(value = "SELECT e FROM EvaluacionEntity e " +
            "JOIN e.periodoEntity p " +
            "JOIN e.alumnoEntity a " +
            "JOIN a.usuarioEntity u " +
            "WHERE e.status = :status " +
            "AND a.uniqueIdentifier = :alumno " +
            "AND p.uniqueIdentifier = :periodo " +
            "AND (e.date like concat('%', :filter, '%') " +
            "OR u.name like concat('%', :filter, '%') " +
            "OR u.pa_surname like concat('%', :filter, '%') " +
            "OR u.numdoc like concat('%', :filter, '%')) ")
    Optional<List<EvaluacionEntity>> findEvaluacionEntities(String status, String filter, String periodo, String alumno, Pageable pageable);
    @Query(value = "SELECT count(e) FROM EvaluacionEntity e " +
            "JOIN e.periodoEntity p " +
            "JOIN e.alumnoEntity a " +
            "JOIN a.usuarioEntity u " +
            "WHERE e.status = :status " +
            "AND a.uniqueIdentifier = :alumno " +
            "AND p.uniqueIdentifier = :periodo " +
            "AND (e.date like concat('%', :filter, '%') " +
            "OR u.name like concat('%', :filter, '%') " +
            "OR u.pa_surname like concat('%', :filter, '%') " +
            "OR u.numdoc like concat('%', :filter, '%')) ")
    Long findCountEvaluacionEntities(String status, String filter, String periodo, String alumno);

    @Query(value = "select e from EvaluacionEntity e JOIN e.alumnoEntity a JOIN e.docentexCursoEntity dc " +
            "JOIN e.periodoEntity p " +
            "WHERE a = e.alumnoEntity and dc = e.docentexCursoEntity and p = e.periodoEntity " +
            "and e.status = :status and a.status = :status and dc.status = :status " +
            "and (a.code like concat('%', :filter, '%') or dc.code like concat('%', :filter, '%') or " +
            "a.uniqueIdentifier like concat('%', :filter, '%') or p.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<EvaluacionEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "select count(e) from EvaluacionEntity e JOIN e.alumnoEntity a JOIN e.docentexCursoEntity dc " +
            "JOIN e.periodoEntity p "+
            "WHERE a = e.alumnoEntity and dc = e.docentexCursoEntity and p = e.periodoEntity " +
            "and e.status = :status and a.status = :status and dc.status = :status " +
            "and (a.code like concat('%', :filter, '%') or dc.code like concat('%', :filter, '%') or " +
            "a.uniqueIdentifier like concat('%', :filter, '%') or p.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    @Query(value = "Select count(*) from evaluacion e " +
            "join periodo p on e.periodo_id = p.id " +
            "join docentexcurso dxc on dxc.id = e.docentexcurso_id " +
            "join curso c on c.id = dxc.curso_id " +
            "join aula a on a.id = dxc.aula_id " +
            "join alumno al on al.id = e.alumno_id " +
            "join user u on u.id = al.user_id " +
            "where p.tx_status = :status " +
            "AND e.tx_status = :status " +
            "AND c.tx_status = :status " +
            "AND al.tx_status = :status " +
            "AND a.tx_status = :status " +
            "AND u.tx_status = :status " +
            "AND dxc.tx_status = :status " +
            "and (u.numdoc like concat('%', :filter, '%') " +
            "or u.pa_surname like concat('%', :filter, '%') " +
            "or u.name like concat('%', :filter, '%') " +
            "or u.ma_surname like concat('%', :filter, '%'))  " +
            "and  c.tx_unique_identifier like concat('%', :curso, '%') " +
            "and p.tx_unique_identifier  like concat('%', :periodo, '%') " +
            "and a.tx_unique_identifier  like concat('%', :aula, '%') " +
            "and e.tx_status = :status", nativeQuery = true)
    Long findCountEntities(String filter, String status, String periodo, String aula, String curso);


    Optional<EvaluacionEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "SELECT e FROM PeriodoEntity p " +
            "JOIN p.evaluacionEntities e " +
            "WHERE p.uniqueIdentifier= :id_periodo " +
            "AND e.status= :status " +
            "AND p.status= :status ")
    Optional<List<EvaluacionEntity>> findById_Periodo(String id_periodo, String status);
    @Query(value = "SELECT e FROM DocentexCursoEntity d " +
            "JOIN d.evaluacionEntities e " +
            "WHERE d=e.docentexCursoEntity " +
            "AND d.uniqueIdentifier= :id_dxc " +
            "AND e.status= :status " +
            "AND d.status= :status ")
    Optional<List<EvaluacionEntity>> findById_DXC(String id_dxc, String status);

    @Query(value = "Select c.name, e.note from curso c join docentexcurso dxc on c.id = dxc.curso_id " +
            "join evaluacion e on dxc.id = e.docentexcurso_id join periodo p on p.id = e.periodo_id " +
            "join alumno a on a.id = e.alumno_id join anio_lectivo al on al.id = p.anio_lectivo_id " +
            "where a.tx_unique_identifier = :alumno " +
            "and p.tx_unique_identifier = :periodo ", nativeQuery = true)
    List<Object[]> findByAlumnoPeriodoAnio(String alumno, String periodo);

    @Query(value = "SELECT concat(u.pa_surname, ' ', u.ma_surname, ' ',u.name), e.note from user u "+
            "join alumno a on a.user_id = u.id "+
            "join evaluacion e on e.alumno_id = a.id "+
            "join periodo p on p.id = e.periodo_id "+
            "join docentexcurso dc on dc.id = e.docentexcurso_id "+
            "join aula al on al.id = dc.aula_id "+
            "join curso c on c.id = dc.curso_id "+
            "join anio_lectivo an on an.id = p.anio_lectivo_id "+
            "where al.tx_unique_identifier = :aula and "+
            "c.tx_unique_identifier = :curso and "+
            "p.tx_unique_identifier = :periodo", nativeQuery = true)
    List<Object[]> findByCursoPeriodoAnio(String curso, String aula, String periodo);

}
