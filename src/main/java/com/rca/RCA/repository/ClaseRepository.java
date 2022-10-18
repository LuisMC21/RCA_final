package com.rca.RCA.repository;

import com.rca.RCA.entity.ClaseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<ClaseEntity, Integer> {

    @Query(value = "select c from ClaseEntity c " +
            "where c.status = :status " +
            "and ( c.code like concat('%', :filter, '%'))")
    Optional<List<ClaseEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "select count(c) from ClaseEntity c " +
            "where c.status = :status " +
            "and ( c.code like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    Optional<ClaseEntity> findByUniqueIdentifier(String uniqueIdentifier);
}
