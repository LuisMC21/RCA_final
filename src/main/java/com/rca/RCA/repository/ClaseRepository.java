package com.rca.RCA.repository;

import com.rca.RCA.entity.ClaseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<ClaseEntity, Integer> {

    //Función para listar las clases según el codigo de docentexCurso
    @Query(value = "select c from ClaseEntity c JOIN c.docentexCursoEntity dc WHERE dc = c.docentexCursoEntity and " +
            "c.status = :status " +
            "and ( dc.code like concat('%', :filter, '%'))")
    Optional<List<ClaseEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar las clases
    @Query(value = "select count(c) from ClaseEntity c JOIN c.docentexCursoEntity dc WHERE dc = c.docentexCursoEntity and " +
            "c.status = :status " +
            "and ( dc.code like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    Optional<ClaseEntity> findByUniqueIdentifier(String uniqueIdentifier);
}
