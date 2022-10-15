package com.rca.RCA.repository;

import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.entity.RolEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    Optional<RolEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "select r from RolEntity r " +
            "where r.status = :status " +
            "and ( r.code like concat('%', :filter, '%') or r.name like concat('%', :filter, '%') ) " +
            "order by r.name")
    Optional<List<RolEntity>> findEntities(String status, String filter, Pageable pageable);
    @Query(value = "select count(r) from RolEntity r " +
            "where r.status = :status " +
            "and ( r.code like concat('%', :filter, '%') or r.name like concat('%', :filter, '%') ) " +
            "order by r.name")
    Long findCountEntities(String status, String filter);

    Optional<RolEntity> findByName(String name);

    @Query(value = "select r from RolEntity r " +
            "where r.name = :name and r.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<RolEntity> findByName(String name, String uniqueIdentifier);
}
