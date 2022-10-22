package com.rca.RCA.repository;

import com.rca.RCA.entity.ApoderadoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<ApoderadoEntity, Integer> {
    @Query(value = "SELECT a FROM UsuarioEntity u " +
            "JOIN u.apoderadoEntity a " +
            "WHERE u = a.usuarioEntity " +
            "AND a.status = :status " +
            "AND u.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or " +
    "       u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Optional<List<ApoderadoEntity>> findEntities(String status, String filter, Pageable pageable);
    @Query(value = "SELECT count(a) FROM UsuarioEntity u " +
            "JOIN u.apoderadoEntity a " +
            "WHERE u = a.usuarioEntity " +
            "AND a.status = :status " +
            "AND u.status = :status " +
            "AND (a.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') "+
            "or u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);


    Optional<ApoderadoEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<ApoderadoEntity> findByEmail(String email);

    @Query(value = "select a from ApoderadoEntity a " +
            "where a.email = :email and a.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<ApoderadoEntity> findByEmail(String email, String uniqueIdentifier);
}
