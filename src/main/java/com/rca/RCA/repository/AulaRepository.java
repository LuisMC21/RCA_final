package com.rca.RCA.repository;

import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.entity.AulaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AulaRepository extends JpaRepository<AulaEntity, Integer> {

    //Función para contar las aulas existentes y activas de un grado, con filtro de código y nombre
     @Query(value = "SELECT count(s) from GradoEntity g " +
            "JOIN g.seccionxGradoEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND g.uniqueIdentifier = :id " +
            "AND s.status = :status " +
            "AND (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))")
    Long findCountAula(String id, String status, String filter);

    //Función para listar las aulas existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT s from GradoEntity g " +
            "JOIN g.seccionxGradoEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND g.uniqueIdentifier = :id " +
            "AND s.status = :status " +
            "AND (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))")
    Optional<List<SeccionEntity>> findAula(String id, String status, String filter, Pageable pageable);

    //Función para obtener un aula con su Identificado Único
    Optional<AulaEntity> findByUniqueIdentifier(String uniqueIdentifier);
}
