package com.rca.RCA.repository;

import com.rca.RCA.entity.NoticiaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticiaRepository extends JpaRepository<NoticiaEntity, Integer> {

    @Query(value = "select n from NoticiaEntity n " +
            "where n.status = :status " +
            "and ( n.code like concat('%', :filter, '%') or n.title like concat('%', :filter, '%') ) " +
            "order by n.title")
    Optional<List<NoticiaEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "select count(c) from NoticiaEntity n " +
            "where n.status = :status " +
            "and ( n.code like concat('%', :filter, '%') or n.title like concat('%', :filter, '%') ) " +
            "order by n.title")
    Long findCountEntities(String status, String filter);


    Optional<NoticiaEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<NoticiaEntity> findByTitle(String title);

    @Query(value = "select n from NoticiaEntity n " +
            "where n.num_doc = :num_doc and n.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<NoticiaEntity> findByTitle(String title, String uniqueIdentifier);
}
