package com.rca.RCA.repository;


import com.rca.RCA.entity.UsuarioEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    @Query(value = "select u from UsuarioEntity u " +
            "where u.status = :status " +
            "and ( u.code like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') ) " +
            "order by u.name")
    Optional<List<UsuarioEntity>> findEntities(String status, String filter, Pageable pageable);
    @Query(value = "select count(c) from UsuarioEntity u " +
            "where u.status = :status " +
            "and ( u.code like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') ) " +
            "order by u.name")
    Long findCountEntities(String status, String filter);


    Optional<UsuarioEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<UsuarioEntity> findByNum_doc(String num_doc);

    @Query(value = "select u from UsuarioEntity u " +
            "where u.num_doc = :num_doc and u.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<UsuarioEntity> findByNum_doc(String num_doc, String uniqueIdentifier);


}
