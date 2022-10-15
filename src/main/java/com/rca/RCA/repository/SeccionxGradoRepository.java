package com.rca.RCA.repository;

import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.entity.SeccionxGradoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SeccionxGradoRepository extends JpaRepository<SeccionxGradoEntity, Integer> {

    //Función para contar las secciones existentes y activas de un grado, con filtro de código y nombre
     @Query(value = "SELECT count(s) from GradoEntity g " +
            "JOIN g.seccionxGradoEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND g.uniqueIdentifier = :id " +
            "AND s.status = :status " +
            "AND (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))")
    Long findCountSeccionxGrado(String id, String status, String filter);

    //Función para listar las secciones existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT s from GradoEntity g " +
            "JOIN g.seccionxGradoEntities x " +
            "JOIN x.seccionEntity s " +
            "WHERE g=x.gradoEntity " +
            "AND g.uniqueIdentifier = :id " +
            "AND s.status = :status " +
            "AND (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))")
    Optional<List<SeccionEntity>> findSeccionxGrado(String id, String status, String filter, Pageable pageable);

    //Función para obtener un grado con su Identificado Único
    Optional<SeccionxGradoEntity> findByUniqueIdentifier(String uniqueIdentifier);
}
