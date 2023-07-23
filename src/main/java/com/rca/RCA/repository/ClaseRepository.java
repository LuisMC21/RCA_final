package com.rca.RCA.repository;

import com.rca.RCA.entity.AlumnoEntity;
import com.rca.RCA.entity.ClaseEntity;
import com.rca.RCA.entity.PeriodoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<ClaseEntity, Integer> {

    //Función para listar las clases según el codigo de docentexCurso
    @Query(value = "select c from ClaseEntity c JOIN c.docentexCursoEntity dc WHERE dc = c.docentexCursoEntity and " +
            "c.status = :status and dc.status = :status " +
            "and ( dc.code like concat('%', :filter, '%'))")
    Optional<List<ClaseEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar las clases
    @Query(value = "select count(c) from ClaseEntity c JOIN c.docentexCursoEntity dc WHERE dc = c.docentexCursoEntity and " +
            "c.status = :status and dc.status = :status " +
            "and ( dc.code like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    @Query(value = "Select c.* from clase c " +
            "join periodo p on p.id = c.periodo_id " +
            "join docentexcurso dxc on dxc.id = c.docentexcurso_id " +
            "join curso cu on cu.id = dxc.curso_id " +
            "join aula a on a.id = dxc.aula_id " +
            "Where c.tx_status = :status and dxc.tx_status = :status and cu.tx_status =:status " +
            "and a.tx_status =:status and p.tx_status=:status " +
            "and (c.name like concat('%', :filter, '%') " +
            "or c.date like concat('%', :filter, '%')) " +
            "and a.tx_unique_identifier like concat ('%',:aula,'%') " +
            "and cu.tx_unique_identifier like concat ('%',:curso,'%') " +
            "and p.tx_unique_identifier like concat ('%',:periodo,'%') and c.tx_status=:status", nativeQuery = true)
    Optional<List<ClaseEntity>> findEntities(String filter, String status, String periodo, String aula, String curso, Pageable pageable);

    @Query(value = "Select count(*) from clase c " +
            "join periodo p on p.id = c.periodo_id " +
            "join docentexcurso dxc on dxc.id = c.docentexcurso_id " +
            "join curso cu on cu.id = dxc.curso_id " +
            "join aula a on a.id = dxc.aula_id " +
            "Where c.tx_status = :status and dxc.tx_status = :status and cu.tx_status =:status " +
            "and a.tx_status =:status and p.tx_status=:status " +
            "and (c.name like concat('%', :filter, '%') " +
            "or c.date like concat('%', :filter, '%')) " +
            "and a.tx_unique_identifier like concat ('%',:aula,'%') " +
            "and cu.tx_unique_identifier like concat ('%',:curso,'%') " +
            "and p.tx_unique_identifier like concat ('%',:periodo,'%') and c.tx_status=:status", nativeQuery = true)
    Long findCountEntities(String filter, String status, String periodo, String aula, String curso);

    @Query(value = "SELECT al FROM AlumnoEntity al " +
            "JOIN al.matriculaEntities m " +
            "JOIN m.aulaEntity au " +
            "JOIN au.docentexCursoEntities dxc " +
            "JOIN dxc.cursoEntity c " +
            "JOIN dxc.anio_lectivoEntity a " +
            "WHERE al.status = :status " +
            "AND m.status = :status " +
            "AND au.status = :status " +
            "AND dxc.status = :status " +
            "AND c.status = :status " +
            "AND a.status = :status " +
            "AND a.uniqueIdentifier = :anio " +
            "AND au.uniqueIdentifier = :aula " +
            "AND c.uniqueIdentifier = :curso")
    Optional<List<AlumnoEntity>> findAlumnosxClase(String status, String anio, String aula, String curso);



    Optional<ClaseEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "SELECT c FROM PeriodoEntity p " +
            "JOIN p.claseEntities c " +
            "WHERE p=c.periodoEntity " +
            "AND p.uniqueIdentifier = :id_periodo " +
            "AND p.status = :status " +
            "AND c.status= :status ")
    Optional<List<ClaseEntity>> findById_Periodo(String id_periodo, String status);
    @Query(value = "SELECT c FROM DocentexCursoEntity d " +
            "JOIN d.claseEntities c " +
            "WHERE d=c.docentexCursoEntity " +
            "AND d.uniqueIdentifier = :id_dxc " +
            "AND d.status = :status " +
            "AND c.status= :status ")
    Optional<List<ClaseEntity>> findById_DxC(String id_dxc, String status);

    //Función para eliminar las asistencias asociadas a una clase
    @Transactional
    @Modifying
    @Query(value = "update asistencia a JOIN clase c  SET a.tx_status = 'DELETED', a.tx_delete_at = :fecha " +
            "where a.clase_id = c.id " +
            "and c.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void eliminarAsistenias(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha") LocalDateTime fecha);

}
