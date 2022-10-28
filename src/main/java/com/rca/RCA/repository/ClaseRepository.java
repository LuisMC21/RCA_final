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


}
