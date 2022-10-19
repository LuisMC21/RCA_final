package com.rca.RCA.repository;

import com.rca.RCA.entity.DocenteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Integer> {

    //Función para listar los docentes activos con filro de código o nombre
/*    @Query(value = "select d from DocenteEntity d " +
            "where d.status = :status " +
            "and d.code like concat('%', :filter, '%')")
    Optional<List<DocenteEntity>> findDocente(String status, String filter, Pageable pageable);
*/
  @Query(value = "SELECT d, u from DocenteEntity d " +
            "JOIN d.usuarioEntity u " +
            "WHERE u = d.usuarioEntity " +
            "AND d.status = :status " +
            "AND (d.code like concat('%', :filter, '%'))")
    Optional<List<DocenteEntity>> findDocente(String status, String filter, Pageable pageable);



    //Función para contar los docentes activass con filro de código o nombre
    @Query(value = "select count(d) from DocenteEntity d " +
            "where d.status = :status " +
            "and d.code like concat('%', :filter, '%')")
    Long findCountSeccion(String status, String filter);

    //Función para obtener un docente con su Identificado Único
    Optional<DocenteEntity> findByUniqueIdentifier(String uniqueIdentifier);
/*
    //Función para obtener un docente con su nombre
    Optional<DocenteEntity> findByName(Character name);

 */
}