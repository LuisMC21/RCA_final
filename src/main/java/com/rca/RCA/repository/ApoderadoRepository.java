package com.rca.RCA.repository;

import com.rca.RCA.entity.ApoderadoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<ApoderadoEntity, Integer> {
    @Query(value = "select a from ApoderadoEntity a " +
            "where a.status = :status " +
            "and ( a.code like concat('%', :filter, '%') or a.email like concat('%', :filter, '%') ) " +
            "order by a.email")
    Optional<List<ApoderadoEntity>> findEntities(String status, String filter, Pageable pageable);
    @Query(value = "select count(a) from ApoderadoEntity a " +
            "where a.status = :status " +
            "and ( a.code like concat('%', :filter, '%') or a.email like concat('%', :filter, '%') ) " +
            "order by a.email")
    Long findCountEntities(String status, String filter);


    Optional<ApoderadoEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<ApoderadoEntity> findByEmail(String email);

    @Query(value = "select a from ApoderadoEntity a " +
            "where a.email = :email and a.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<ApoderadoEntity> findByEmail(String email, String uniqueIdentifier);
}
