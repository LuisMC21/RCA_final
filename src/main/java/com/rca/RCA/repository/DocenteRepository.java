package com.rca.RCA.repository;

import com.rca.RCA.entity.DocenteEntity;
import com.rca.RCA.entity.UsuarioEntity;
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
            "AND (d.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') " +
            "or u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') " +
            "or u.numdoc like concat('%', :filter, '%') or d.uniqueIdentifier like concat('%', :filter, '%'))")
    Optional<List<DocenteEntity>> findDocente(String status, String filter, Pageable pageable);

  //Función para contar los docentes activass con filro de código, nombre o documento de identidad
    @Query(value = "SELECT count(d) FROM UsuarioEntity u " +
            "JOIN u.docenteEntity d " +
            "WHERE u = d.usuarioEntity " +
            "AND d.status = :status " +
            "AND u.status = :status " +
            "AND (d.code like concat('%', :filter, '%') or u.pa_surname like concat('%', :filter, '%') " +
            "or u.ma_surname like concat('%', :filter, '%') or u.name like concat('%', :filter, '%') " +
            "or u.numdoc like concat('%', :filter, '%') or d.uniqueIdentifier like concat('%', :filter, '%'))")
    Long findCountDocente(String status, String filter);

    //Función para obtener un docente con su Identificado Único
    @Query(value = "SELECT d FROM DocenteEntity d " +
            "WHERE d.uniqueIdentifier = :id " +
            "AND d.status = :status ")
    Optional<DocenteEntity> findByUniqueIdentifier(String id, String status);


    @Query(value = "SELECT d FROM DocenteEntity d " +
            "JOIN d.docentexCursoEntities dxc " +
            "JOIN dxc.aulaEntity au " +
            "JOIN dxc.cursoEntity c " +
            "JOIN au.matriculaEntities m " +
            "JOIN m.anio_lectivoEntity an " +
            "WHERE d.status = :status " +
            "AND c.uniqueIdentifier = :id_curso " +
            "AND au.uniqueIdentifier = :id_aula " +
            "AND an.uniqueIdentifier = :id_anio ")
    Optional<DocenteEntity> findAulaAnio(String id_aula, String id_curso, String id_anio, String status);
    @Query(value = "SELECT d FROM DocenteEntity d " +
            "JOIN d.docentexCursoEntities dxc " +
            "JOIN dxc.aulaEntity au " +
            "JOIN dxc.cursoEntity c " +
            "JOIN au.matriculaEntities m " +
            "JOIN m.anio_lectivoEntity an " +
            "WHERE d.status = :status " +
            "AND c.uniqueIdentifier = :id_curso " +
            "AND au.uniqueIdentifier = :id_aula " +
            "AND an.uniqueIdentifier = :id_anio ")
    Optional<List<DocenteEntity>> findOneAulaAnio(String id_aula, String id_curso, String id_anio, String status);

  @Query(value = "SELECT u FROM UsuarioEntity u " +
          "JOIN u.docenteEntity d " +
          "WHERE d.uniqueIdentifier = :id_docente " +
          "AND d.status = :status " +
          "AND u.status = :status ")
  Optional<UsuarioEntity> findUserByDocente(String id_docente, String status);

}