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
  @Query(value = "SELECT d FROM UsuarioEntity u " +
            "JOIN u.docenteEntity d " +
            "WHERE u = d.usuarioEntity " +
            "AND d.status = :status " +
            "AND u.status = :status " +
            "AND (d.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Optional<List<DocenteEntity>> findDocente(String status, String filter, Pageable pageable);

  //Función para contar los docentes activass con filro de código, nombre o documento de identidad
    @Query(value = "SELECT count(d) FROM UsuarioEntity u " +
            "JOIN u.docenteEntity d " +
            "WHERE u = d.usuarioEntity " +
            "AND d.status = :status " +
            "AND u.status = :status " +
            "AND (d.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') or u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') or u.numdoc like concat('%', :filter, '%'))")
    Long findCountDocente(String status, String filter);

    //Función para obtener un docente con su Identificado Único
    @Query(value = "SELECT d FROM DocenteEntity d " +
            "WHERE d.uniqueIdentifier = :id " +
            "AND d.status = :status ")
    Optional<DocenteEntity> findByUniqueIdentifier(String id, String status);
}