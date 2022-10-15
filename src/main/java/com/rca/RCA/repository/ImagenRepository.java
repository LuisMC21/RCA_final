package com.rca.RCA.repository;

import com.rca.RCA.entity.ImagenEntity;
import com.rca.RCA.entity.UsuarioEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImagenRepository extends JpaRepository<ImagenEntity, Integer> {

    @Query(value = "select i from ImagenEntity i " +
            "where i.status = :status " +
            "and ( i.code like concat('%', :filter, '%') or i.name like concat('%', :filter, '%') ) " +
            "order by i.name")
    Optional<List<ImagenEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "select count(i) from ImagenEntity i " +
            "where i.status = :status " +
            "and ( i.code like concat('%', :filter, '%') or i.name like concat('%', :filter, '%') ) " +
            "order by i.name")
    Long findCountEntities(String status, String filter);

    Optional<ImagenEntity> findByUniqueIdentifier(String uniqueIdentifier);

    Optional<ImagenEntity> findByName(String name);

    @Query(value = "select i from ImagenEntity i " +
            "where i.name = :name and i.uniqueIdentifier <> :uniqueIdentifier ")
    Optional<ImagenEntity> findByName(String name, String uniqueIdentifier);
}
