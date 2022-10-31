package com.rca.RCA.repository;

import com.rca.RCA.entity.ClaseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<ClaseEntity, Integer> {

    @Query(value = "SELECT c FROM ClaseEntity c " +
            "WHERE c.status = :status " +
            "AND ( c.code like concat('%', :filter, '%'))")
    Optional<List<ClaseEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "SELECT count(c) FROM ClaseEntity c " +
            "WHERE c.status = :status " +
            "AND ( c.code like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    Optional<ClaseEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "SELECT c FROM AulaEntity a " +
            "JOIN a.claseEntities c " +
            "WHERE a=c.aulaEntity " +
            "AND a.uniqueIdentifier = :id_aula " +
            "AND a.status = :status " +
            "AND c.status= :status ")
    Optional<List<ClaseEntity>> findByAula(String id_aula, String status);
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

}
