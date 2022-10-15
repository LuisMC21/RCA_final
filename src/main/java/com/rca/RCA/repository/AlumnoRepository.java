package com.rca.RCA.repository;

import com.rca.RCA.entity.AlumnoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<AlumnoEntity, Integer> {

    @Query(value = "select a from AlumnoEntity a " +
            "where a.status = :status " +
            "and ( a.code like concat('%', :filter, '%') ")
    Optional<List<AlumnoEntity>> findEntities(String status, String filter, Pageable pageable);
    @Query(value = "select count(c) from AlumnoEntity a " +
            "where a.status = :status " +
            "and ( a.code like concat('%', :filter, '%') ")
    Long findCountEntities(String status, String filter);


    Optional<AlumnoEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<AlumnoEntity> findByCode(String code);

    @Query(value = "select a from AlumnoEntity a " +
            "where a.code = :code and u.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<AlumnoEntity> findByCode(String code, String uniqueIdentifier);
}
